package cz.muni.fi.one.pools;

import cz.muni.fi.one.mappers.HostMapper;
import cz.muni.fi.scheduler.elementpools.IHostPool;
import cz.muni.fi.scheduler.elements.HostElement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
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
    
    public HostElementPool(Client oneClient) {
        hp = new HostPool(oneClient);
    }
    
    /**
     * Goes through the pool and maps all hosts.
     * 
     * @return list of HostElements
     */
    @Override
    public List<HostElement> getHosts() {
        List<HostElement> hosts = new ArrayList<>();
        OneResponse hpr = hp.info();
        if (hpr.isError()) {
            log.error(hpr.getErrorMessage());
        }
        Iterator<Host> itr = hp.iterator();
        while (itr.hasNext()) {
            Host element = itr.next();
            System.out.println("Host: " + element + "   state: " + element.state() + " id: " + element.getId());
            HostElement h = HostMapper.map(element);
            System.out.println("HostElement: " + h);
            hosts.add(h);
        }
        return hosts;
    }
    
    /**
     * Gets hosts that are active.
     * Host states:  1 = monitoring-monitored
     *               2 = monitored
     * @return list of active hosts
     */
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
    public HostElement getHost(int id) {
        hp.info();
        return HostMapper.map(hp.getById(id));
    }
    
    @Override
    public List<Integer> getHostsIds() {
        List<Integer> hostsIds = new ArrayList<>();
        for(HostElement h: getHosts()) {
            hostsIds.add(h.getId());
        }
        return hostsIds;
    }
}
