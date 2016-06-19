/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.one.pools;

import cz.muni.fi.one.mappers.ClusterMapper;
import cz.muni.fi.scheduler.elementpools.IClusterPool;
import cz.muni.fi.scheduler.resources.ClusterElement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.opennebula.client.Client;
import org.opennebula.client.OneResponse;
import org.opennebula.client.cluster.Cluster;
import org.opennebula.client.cluster.ClusterPool;

/**
 *
 * @author Gabriela Podolnikova
 */
public class ClusterElementPool implements IClusterPool {
    
    private final ClusterPool cp;
    
    public ClusterElementPool(Client oneClient) {
        cp = new ClusterPool(oneClient);
    }

    /**
     * @return the clusters
     */
    @Override
    public List<ClusterElement> getClusters() {
        List<ClusterElement> clusters = new ArrayList<>();
        OneResponse cpr = cp.info();
        if (cpr.isError()) {
            //TODO: log it
            System.out.println(cpr.getErrorMessage());
        }
        Iterator<Cluster> itr = cp.iterator();
        while (itr.hasNext()) {
            Cluster element = itr.next();
            System.out.println("Cluster: " + element);
            ClusterElement c = ClusterMapper.map(element);
            System.out.println("Cluster: " + c);
            clusters.add(c);
        }
        return clusters;
    }

    @Override
    public ClusterElement getCluster(int id) {
        return ClusterMapper.map(cp.getById(id));
    }
}
