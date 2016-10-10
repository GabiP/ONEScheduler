package cz.muni.fi.xml.pools;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import cz.muni.fi.scheduler.elementpools.IHostPool;
import cz.muni.fi.scheduler.resources.HostElement;
import cz.muni.fi.xml.mappers.HostXmlMapper;
import cz.muni.fi.xml.resources.HostXmlList;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Gabriela Podolnikova
 */
public class HostXmlPool implements IHostPool {    
    
    private List<HostElement> hosts;

    public HostXmlPool(String hostPoolPath) throws IOException {
        XmlMapper xmlMapper = new XmlMapper();
        String hostPoolMessage = new String(Files.readAllBytes(Paths.get(hostPoolPath)));
        HostXmlList xmlList = xmlMapper.readValue(hostPoolMessage, HostXmlList.class);
        hosts = HostXmlMapper.map(xmlList.getHosts());
    }

    @Override
    public List<HostElement> getHosts() {
        return Collections.unmodifiableList(hosts);
    }
    
    @Override
    public List<HostElement> getActiveHosts() {
        List<HostElement> activeHosts = new ArrayList<>();
        for (HostElement host: getHosts()) {
            if (host.getState() == 1 || host.getState() == 2) {
                activeHosts.add(host);
            }
        }
        return activeHosts;
    }

    @Override
    public List<Integer> getHostsIds() {
        List<Integer> hostsIds = new ArrayList<>();
        for(HostElement h: getHosts()) {
            hostsIds.add(h.getId());
        }
        return hostsIds;
    }

    @Override
    public HostElement getHost(int id) {
        for (HostElement h : hosts) {
            if (h.getId() == id) {
                return h;
            }
        }
        return null;
    }
    
}
