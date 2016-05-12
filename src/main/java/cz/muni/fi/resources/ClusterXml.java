/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.resources;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.opennebula.client.cluster.Cluster;

/**
 *
 * @author Gabriela Podolnikova
 */
public class ClusterXml {
    
    private int id;
    
    private String name;
    
    private Cluster cluster;
    
    private List<Integer> hosts;
    
    private List<Integer> datastores;
    
    public ClusterXml(Cluster cluster) {
        this.cluster = cluster;
        cluster.info();
        this.init();
    }
    
    public void init() {
        id = Integer.parseInt(cluster.xpath("/CLUSTER/ID"));
        name = cluster.xpath("/CLUSTER/NAME");
        try {
            hosts = NodeElementLoader.getNodeId(cluster, "/CLUSTER/HOSTS/ID");
        } catch (InstantiationException | IllegalAccessException ex) {
            // TODO: react on failure if needed
            Logger.getLogger(VmXml.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            datastores = NodeElementLoader.getNodeId(cluster, "/CLUSTER/DATASTORES/ID");
        } catch (InstantiationException | IllegalAccessException ex) {
            // TODO: react on failure if needed
            Logger.getLogger(VmXml.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public String toString() {
        return "Cluster{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
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

    /**
     * @return the hosts
     */
    public List<Integer> getHosts() {
        return hosts;
    }

    /**
     * @return the datastores
     */
    public List<Integer> getDatastores() {
        return datastores;
    }
    
    
}
