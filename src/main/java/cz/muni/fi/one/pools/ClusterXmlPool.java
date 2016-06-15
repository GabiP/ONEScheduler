/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.one.pools;

import cz.muni.fi.scheduler.resources.ClusterXml;
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
    
    private final ClusterPool cp;
    
    private ArrayList<ClusterXml> clusters;
    
    public ClusterXmlPool(Client oneClient) {
        cp = new ClusterPool(oneClient);
    }
    
    public void loadClusters() {
        clusters = new ArrayList<>();
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
    }
    
    public ClusterXml getById(Integer id) {
        for (ClusterXml cluster: clusters) {
            if (cluster.getId() == id) {
                return cluster;
            }
        }
        return null;
    }
    /**
     * @return the clusters
     */
    public ArrayList<ClusterXml> getClusters() {
        return clusters;
    }
}
