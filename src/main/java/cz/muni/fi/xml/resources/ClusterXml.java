/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.xml.resources;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.util.List;

/**
 *
 * @author Gabriela Podolnikova
 */
public class ClusterXml {
    
    @JacksonXmlProperty(localName = "ID")
    private Integer id;
    
    @JacksonXmlProperty(localName = "NAME")
    private String name;    
    
    @JacksonXmlProperty(localName = "HOSTS")
    private List<Integer> hosts;
    
    @JacksonXmlProperty(localName = "DATASTORES")
    private List<Integer> datastores;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Integer> getHosts() {
        return hosts;
    }

    public void setHosts(List<Integer> hosts) {
        this.hosts = hosts;
    }

    public List<Integer> getDatastores() {
        return datastores;
    }

    public void setDatastores(List<Integer> datastores) {
        this.datastores = datastores;
    }

    @Override
    public String toString() {
        return "ClusterXml{" + "id=" + id + ", name=" + name + ", hosts=" + hosts + ", datastores=" + datastores + '}';
    }
    
}
