package cz.muni.fi.one.pools;

import cz.muni.fi.one.mappers.HostMapper;
import cz.muni.fi.scheduler.elementpools.IHostPool;
import cz.muni.fi.scheduler.elements.HostElement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.opennebula.client.Client;
import org.opennebula.client.OneResponse;
import org.opennebula.client.host.Host;
import org.opennebula.client.host.HostPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class represents OpenNebula's HostPool containing all instances of hosts in the system.
 * The pool is accessed through OpenNebula's Client. The Client represents the connection with the core of OpenNebula.
 * Each OpenNebula's instance of Host is mapped to our HostElement.
 * 
 * @author Gabriela Podolnikova
 */
public class HostElementPool implements IHostPool{
    
    protected final Logger log = LoggerFactory.getLogger(getClass());
    
    private HostPool hp;
    
    private List<HostElement> hosts;
    
    public HostElementPool(Client oneClient) {
        hp = new HostPool(oneClient);
        hosts = new ArrayList<>();
        OneResponse hpr = hp.info();
        if (hpr.isError()) {
            log.error(hpr.getErrorMessage());
        }
        Iterator<Host> itr = hp.iterator();
        while (itr.hasNext()) {
            Host element = itr.next();
            HostElement h = HostMapper.map(element);
            hosts.add(h);
        }
    }
    
    /**
     * Gets all cached hosts.
     * 
     * @return list of HostElements
     */
    @Override
    public List<HostElement> getHosts() {
        return Collections.unmodifiableList(hosts);
    }
    
    /**
     * Gets hosts that are active.
     * Host states:  1 = monitoring-monitored
     *               2 = monitored
     * @return list of active hosts
     */
    @Override
    public List<HostElement> getActiveHosts() {
        return getHosts().stream().filter(host -> host.getState() == 1 || host.getState() == 2).collect(Collectors.toList());
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
    
    @Override
    public List<Integer> getHostsIds() {
        return getHosts().stream().map(HostElement::getId).collect(Collectors.toList());
    }
}
