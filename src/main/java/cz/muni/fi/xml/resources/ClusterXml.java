package cz.muni.fi.xml.resources;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.util.List;

/**
 * This class represents the Cluster retrived from xml.
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
    
    @JacksonXmlProperty(localName = "RESERVED_CPU")
    private Float reservedCpu;
    
    @JacksonXmlProperty(localName = "RESERVED_MEM")
    private Integer reservedMemory;

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

    public Float getReservedCpu() {
        return reservedCpu;
    }

    public void setReservedCpu(Float reservedCpu) {
        this.reservedCpu = reservedCpu;
    }

    public Integer getReservedMemory() {
        return reservedMemory;
    }

    public void setReservedMemory(Integer reservedMemory) {
        this.reservedMemory = reservedMemory;
    }

    @Override
    public String toString() {
        return "ClusterXml{" + "id=" + id + ", name=" + name + ", hosts=" + hosts + ", datastores=" + datastores + '}';
    }
    
}
