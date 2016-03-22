/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.pools;

import cz.muni.fi.resources.HostXml;
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
    
    private ArrayList<HostXml> hosts;
    
    public ArrayList<HostXml> loadHosts(Client oneClient) {
        setHosts(new ArrayList<>());
        hp = new HostPool(oneClient);
        OneResponse hpr = hp.info();
        if (hpr.isError()) {
            //TODO: log it
            System.out.println(hpr.getErrorMessage());
        }
        Iterator<Host> itr = hp.iterator();
        while (itr.hasNext()) {
            Host element = itr.next();
            System.out.println("Host: " + element + "   state: " + element.state());
            if (element.state() == 1 || element.state() == 2) {
                HostXml h = new HostXml(element);
                getHosts().add(h);
            }
        }
        return getHosts();
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
    public ArrayList<HostXml> getHosts() {
        return hosts;
    }

    /**
     * @param hosts the hosts to set
     */
    public void setHosts(ArrayList<HostXml> hosts) {
        this.hosts = hosts;
    }
}
