/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.one.pools;

import cz.muni.fi.one.mappers.HostMapper;
import cz.muni.fi.scheduler.elementpools.IHostPool;
import cz.muni.fi.scheduler.resources.HostElement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.opennebula.client.Client;
import org.opennebula.client.OneResponse;
import org.opennebula.client.host.Host;
import org.opennebula.client.host.HostPool;

/**
 *
 * @author Gabriela Podolnikova
 */
public class HostElementPool implements IHostPool{
    
    private HostPool hp;
    
    public HostElementPool(Client oneClient) {
        hp = new HostPool(oneClient);
    }
    
    /**
     * Loads all hosts from OpenNebula HostPool.
     * Retrieves and store the xml representation as HostXml object into an array of hosts.
     * @return array of hosts
     */
    @Override
    public List<HostElement> getHosts() {
        List<HostElement> hosts = new ArrayList<>();
        OneResponse hpr = hp.info();
        if (hpr.isError()) {
            //TODO: log it
            System.out.println(hpr.getErrorMessage());
        }
        Iterator<Host> itr = hp.iterator();
        while (itr.hasNext()) {
            Host element = itr.next();
            System.out.println("Host: " + element + "   state: " + element.state() + " id: " + element.getId());
            HostElement h = HostMapper.map(element);
            getHosts().add(h);
        }
        return hosts;
    }
    
    /**
     * Gets hosts that are active.
     * Host states:  1 = monitoring-monitored
     *               2 = monitored
     * @return array of active hosts
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
