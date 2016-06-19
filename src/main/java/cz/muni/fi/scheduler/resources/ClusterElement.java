/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.scheduler.resources;

import java.util.List;

/**
 *
 * @author Gabriela Podolnikova
 */
public class ClusterElement {

    private Integer id;
    
    private String name;    
    
    private List<Integer> hosts;
    
    private List<Integer> datastores;    
        
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
    public Integer getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Integer id) {
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
     * @return the hosts
     */
    public List<Integer> getHosts() {
        return hosts;
    }

    public void setHosts(List<Integer> hosts) {
        this.hosts = hosts;
    }
    
    /**
     * @return the datastores
     */
    public List<Integer> getDatastores() {
        return datastores;
    }    
    
    public void setDatastores(List<Integer> datastores) {
        this.datastores = datastores;
    }
}
