/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.resources;

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
    
    private ArrayList<Integer> hosts;
    
    private ArrayList<Integer> datastores;
    
    public ClusterXml(Cluster cluster) {
        this.cluster = cluster;
        cluster.info();
        this.init();
    }
    
    public void init() {
        id = Integer.parseInt(cluster.xpath("/CLUSTER/ID"));
        name = cluster.xpath("/CLUSTER/NAME");
        get_hosts("/CLUSTER/HOSTS/ID");
        get_ds("/CLUSTER/DATASTORES/ID");
    }

    public void get_hosts(String xpathExpr) {
        hosts = new ArrayList<>();
        System.out.println("Inside get hosts: " + xpathExpr);
        int i = 1;
        String idnode = cluster.xpath(xpathExpr + "["+i+"]");
        while (!idnode.equals("")) {
            System.out.println("host id: " + idnode);
            hosts.add(Integer.parseInt(idnode));
            i++;
            idnode = cluster.xpath(xpathExpr + "["+i+"]");
        }
    }
    
    public void get_ds(String xpathExpr) {
        datastores = new ArrayList<>();
        System.out.println("Inside get ds: " + xpathExpr);
        int i = 1;
        String idnode = cluster.xpath(xpathExpr + "["+i+"]");
        while (!idnode.equals("")) {
            System.out.println("host id: " + idnode);
            datastores.add(Integer.parseInt(idnode));
            i++;
            idnode = cluster.xpath(xpathExpr + "["+i+"]");
        }
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
    public ArrayList<Integer> getHosts() {
        return hosts;
    }

    /**
     * @return the datastores
     */
    public ArrayList<Integer> getDatastores() {
        return datastores;
    }
    
    
}
