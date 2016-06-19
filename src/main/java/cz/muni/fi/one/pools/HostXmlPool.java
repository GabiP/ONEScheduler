/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.one.pools;

import cz.muni.fi.scheduler.resources.HostElement;
import java.util.ArrayList;
import java.util.Iterator;
import org.opennebula.client.Client;
import org.opennebula.client.OneResponse;
import org.opennebula.client.host.Host;
import org.opennebula.client.host.HostPool;

/**
 *
 * @author Gabriela Podolnikova
 */
public class HostXmlPool {
    
    private HostPool hp;
    
    private ArrayList<HostElement> hosts;
    
    public HostXmlPool(Client oneClient) {
        hp = new HostPool(oneClient);
    }
    
    /**
     * Loads all hosts from OpenNebula HostPool.
     * Retrieves and store the xml representation as HostXml object into an array of hosts.
     * @return array of hosts
     */
    public void loadHosts() {
        hosts = new ArrayList<>();
        OneResponse hpr = hp.info();
        if (hpr.isError()) {
            //TODO: log it
            System.out.println(hpr.getErrorMessage());
        }
        Iterator<Host> itr = hp.iterator();
        while (itr.hasNext()) {
            Host element = itr.next();
            System.out.println("Host: " + element + "   state: " + element.state() + " id: " + element.getId());
            HostElement h = new HostElement(element);
            getHosts().add(h);
        }
    }
    
    /**
     * Gets hosts that are active.
     * Host states:  1 = monitoring-monitored
     *               2 = monitored
     * @return array of active hosts
     */
    public ArrayList<HostElement> getActiveHosts() {
        ArrayList<HostElement> activeHosts = new ArrayList<>();
        for (HostElement host: activeHosts) {
            if (host.getState() == 1 || host.getState() == 2) {
                activeHosts.add(host);
            }
        }
        return activeHosts;
    }

    /**
     * @return the hp
     */
    public HostPool getHp() {
        return hp;
    }

    /**
     * @param hp the hp to set
     */
    public void setHp(HostPool hp) {
        this.hp = hp;
    }

    /**
     * @return the hosts
     */
    public ArrayList<HostElement> getHosts() {
        return hosts;
    }

    /**
     * @param hosts the hosts to set
     */
    public void setHosts(ArrayList<HostElement> hosts) {
        this.hosts = hosts;
    }
    
    public HostElement getById(Integer id) {
        for (HostElement host: hosts) {
            if (host.getId() == id) {
                return host;
            }
        }
        return null;
    }
    
    public ArrayList<Integer> getHostsIds() {
        ArrayList<Integer> hostsIds = new ArrayList<>();
        for(HostElement h: hosts) {
            hostsIds.add(h.getId());
        }
        return hostsIds;
    }
}
