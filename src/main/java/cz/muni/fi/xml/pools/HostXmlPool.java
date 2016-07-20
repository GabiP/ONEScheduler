package cz.muni.fi.xml.pools;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import cz.muni.fi.scheduler.elementpools.IHostPool;
import cz.muni.fi.scheduler.resources.HostElement;
import cz.muni.fi.xml.mappers.HostXmlMapper;
import cz.muni.fi.xml.resources.HostXml;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Gabriela Podolnikova
 */
@JacksonXmlRootElement(localName = "HOSTPOOL")
public class HostXmlPool implements IHostPool {    
    
    @JacksonXmlProperty(localName = "HOST")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<HostXml> hosts;

    @Override
    public List<HostElement> getHosts() {
        return Collections.unmodifiableList(HostXmlMapper.map(hosts));
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
        for (HostElement h : HostXmlMapper.map(hosts)) {
            if (h.getId() == id) {
                return h;
            }
        }
        return null;
    }
    
}
