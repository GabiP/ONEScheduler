package cz.muni.fi.xml.pools;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import cz.muni.fi.scheduler.elementpools.IHostPool;
import cz.muni.fi.scheduler.elements.HostElement;
import cz.muni.fi.xml.mappers.HostXmlMapper;
import cz.muni.fi.xml.resources.lists.HostXmlList;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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
        return getHosts().stream().filter(host -> host.getState() == 1 || host.getState() == 2).collect(Collectors.toList());
    }

    @Override
    public List<Integer> getHostsIds() {
        return getHosts().stream().map(HostElement::getId).collect(Collectors.toList());
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
