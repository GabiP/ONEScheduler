/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.one.pools;

import cz.muni.fi.scheduler.resources.ClusterElement;
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
    
    private ArrayList<ClusterElement> clusters;
    
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
            ClusterElement c = new ClusterElement(element);
            System.out.println("Cluster: " + c);
            clusters.add(c);
        }
    }
    
    public ClusterElement getById(Integer id) {
        for (ClusterElement cluster: clusters) {
            if (cluster.getId() == id) {
                return cluster;
            }
        }
        return null;
    }
    /**
     * @return the clusters
     */
    public ArrayList<ClusterElement> getClusters() {
        return clusters;
    }
}
