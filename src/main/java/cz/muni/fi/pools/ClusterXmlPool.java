/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.pools;

import cz.muni.fi.resources.ClusterXml;
import java.util.ArrayList;
import java.util.Iterator;
import org.opennebula.client.Client;
import org.opennebula.client.OneResponse;
import org.opennebula.client.cluster.Cluster;
import org.opennebula.client.cluster.ClusterPool;

/**
 *
 * @author Gabriela Podolnikova
 */
public class ClusterXmlPool {
    
    private ClusterPool cp;
    
    private ArrayList<ClusterXml> clusters;
    
    public ArrayList<ClusterXml> loadClusters(Client oneClient) {
        clusters = new ArrayList<>();
        cp = new ClusterPool(oneClient);
        OneResponse cpr = cp.info();
        if (cpr.isError()) {
            //TODO: log it
            System.out.println(cpr.getErrorMessage());
        }
        Iterator<Cluster> itr = cp.iterator();
        while (itr.hasNext()) {
            Cluster element = itr.next();
            System.out.println("Cluster: " + element);
            ClusterXml c = new ClusterXml(element);
            System.out.println("Cluster: " + c);
            clusters.add(c);
        }
        return clusters;
    }
}
