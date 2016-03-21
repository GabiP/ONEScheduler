/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.pools;

import java.util.ArrayList;
import org.opennebula.client.cluster.Cluster;

/**
 *
 * @author Gabriela Podolnikova
 */
public class ClusterXml {
    
    private int id;
    
    private String name;
    
    private Cluster cluster;
    
    public ClusterXml(Cluster cluster) {
        this.cluster = cluster;
        cluster.info();
        this.init();
    }
    
    public void init() {
        id = Integer.parseInt(cluster.xpath("/CLUSTER/ID"));
        name = cluster.xpath("/CLUSTER/NAME");
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the cluster
     */
    public Cluster getCluster() {
        return cluster;
    }

    /**
     * @param cluster the cluster to set
     */
    public void setCluster(Cluster cluster) {
        this.cluster = cluster;
    }
    
    
}
