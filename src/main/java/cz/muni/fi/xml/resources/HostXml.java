/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.xml.resources;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import cz.muni.fi.scheduler.resources.nodes.DatastoreNode;
import cz.muni.fi.scheduler.resources.nodes.PciNode;
import java.util.List;

/**
 *
 * @author gabi
 */
public class HostXml {
    @JacksonXmlProperty(localName = "ID")
    private Integer id;

    @JacksonXmlProperty(localName = "NAME")
    private String name;

    @JacksonXmlProperty(localName = "STATE")
    private int state;
    
    @JacksonXmlProperty(localName = "CLUSTER_ID")
    private Integer clusterId;
    
    @JacksonXmlProperty(localName = "DISK_USAGE")
    private Integer disk_usage;
    
    @JacksonXmlProperty(localName = "MEM_USAGE")
    private Integer mem_usage;
    
    @JacksonXmlProperty(localName = "CPU_USAGE")
    private Float cpu_usage;

    private Integer max_disk;

    private Integer max_mem;

    private Float max_cpu;

    private Integer free_disk;
    
    private Integer free_mem;

    private Float free_cpu;

    private Integer used_disk;
    
    private Integer used_mem;
    
    private Integer used_cpu;
      
    private Integer runningVms;
    
    private Integer reservedCpu;
    
    private Integer reservedMemory;
        
    @JacksonXmlProperty(localName = "PCIS")
    private PciNodeXml[] pcis;
    
    @JacksonXmlProperty(localName = "DATASTORES")
    private DatastoreNodeXml[] datastores;

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

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    @Override
    public String toString() {
        String text =  "HostXml{" + "id=" + id + ", name=" + name + ", state=" + state + ", clusterId=" + clusterId + ", disk_usage=" + disk_usage + ", mem_usage=" + mem_usage + ", cpu_usage=" + cpu_usage + ", max_disk=" + max_disk + ", max_mem=" + max_mem + ", max_cpu=" + max_cpu + ", free_disk=" + free_disk + ", free_mem=" + free_mem + ", free_cpu=" + free_cpu + ", used_disk=" + used_disk + ", used_mem=" + used_mem + ", used_cpu=" + used_cpu + ", runningVms=" + runningVms + ", reservedCpu=" + reservedCpu + ", reservedMemory=" + reservedMemory + ", pcis=" + pcis + ", datastores=" + datastores + '}';
        text += "\n\t Datastores: ";
        for(DatastoreNodeXml xml : datastores) {
            text += xml + ", ";
        }
        return text;
    }

    /**
     * @return the clusterId
     */
    public Integer getClusterId() {
        return clusterId;
    }

    /**
     * @param clusterId the clusterId to set
     */
    public void setClusterId(Integer clusterId) {
        this.clusterId = clusterId;
    }

    /**
     * @return the disk_usage
     */
    public Integer getDisk_usage() {
        return disk_usage;
    }

    /**
     * @param disk_usage the disk_usage to set
     */
    public void setDisk_usage(Integer disk_usage) {
        this.disk_usage = disk_usage;
    }

    /**
     * @return the mem_usage
     */
    public Integer getMem_usage() {
        return mem_usage;
    }

    /**
     * @param mem_usage the mem_usage to set
     */
    public void setMem_usage(Integer mem_usage) {
        this.mem_usage = mem_usage;
    }

    /**
     * @return the cpu_usage
     */
    public Float getCpu_usage() {
        return cpu_usage;
    }

    /**
     * @param cpu_usage the cpu_usage to set
     */
    public void setCpu_usage(Float cpu_usage) {
        this.cpu_usage = cpu_usage;
    }

    /**
     * @return the max_disk
     */
    public Integer getMax_disk() {
        return max_disk;
    }

    /**
     * @param max_disk the max_disk to set
     */
    public void setMax_disk(Integer max_disk) {
        this.max_disk = max_disk;
    }

    /**
     * @return the max_mem
     */
    public Integer getMax_mem() {
        return max_mem;
    }

    /**
     * @param max_mem the max_mem to set
     */
    public void setMax_mem(Integer max_mem) {
        this.max_mem = max_mem;
    }

    /**
     * @return the max_cpu
     */
    public Float getMax_cpu() {
        return max_cpu;
    }

    /**
     * @param max_cpu the max_cpu to set
     */
    public void setMax_cpu(Float max_cpu) {
        this.max_cpu = max_cpu;
    }

    /**
     * @return the free_disk
     */
    public Integer getFree_disk() {
        return free_disk;
    }

    /**
     * @param free_disk the free_disk to set
     */
    public void setFree_disk(Integer free_disk) {
        this.free_disk = free_disk;
    }

    /**
     * @return the free_mem
     */
    public Integer getFree_mem() {
        return free_mem;
    }

    /**
     * @param free_mem the free_mem to set
     */
    public void setFree_mem(Integer free_mem) {
        this.free_mem = free_mem;
    }

    /**
     * @return the free_cpu
     */
    public Float getFree_cpu() {
        return free_cpu;
    }

    /**
     * @param free_cpu the free_cpu to set
     */
    public void setFree_cpu(Float free_cpu) {
        this.free_cpu = free_cpu;
    }

    /**
     * @return the used_disk
     */
    public Integer getUsed_disk() {
        return used_disk;
    }

    /**
     * @param used_disk the used_disk to set
     */
    public void setUsed_disk(Integer used_disk) {
        this.used_disk = used_disk;
    }

    /**
     * @return the used_mem
     */
    public Integer getUsed_mem() {
        return used_mem;
    }

    /**
     * @param used_mem the used_mem to set
     */
    public void setUsed_mem(Integer used_mem) {
        this.used_mem = used_mem;
    }

    /**
     * @return the used_cpu
     */
    public Integer getUsed_cpu() {
        return used_cpu;
    }

    /**
     * @param used_cpu the used_cpu to set
     */
    public void setUsed_cpu(Integer used_cpu) {
        this.used_cpu = used_cpu;
    }

    /**
     * @return the runningVms
     */
    public Integer getRunningVms() {
        return runningVms;
    }

    /**
     * @param runningVms the runningVms to set
     */
    public void setRunningVms(Integer runningVms) {
        this.runningVms = runningVms;
    }

    /**
     * @return the reservedCpu
     */
    public Integer getReservedCpu() {
        return reservedCpu;
    }

    /**
     * @param reservedCpu the reservedCpu to set
     */
    public void setReservedCpu(Integer reservedCpu) {
        this.reservedCpu = reservedCpu;
    }

    /**
     * @return the reservedMemory
     */
    public Integer getReservedMemory() {
        return reservedMemory;
    }

    /**
     * @param reservedMemory the reservedMemory to set
     */
    public void setReservedMemory(Integer reservedMemory) {
        this.reservedMemory = reservedMemory;
    }

    public PciNodeXml[] getPcis() {
        return pcis;
    }

    public void setPcis(PciNodeXml[] pcis) {
        this.pcis = pcis;
    }

    public DatastoreNodeXml[] getDatastores() {
        return datastores;
    }

    public void setDatastores(DatastoreNodeXml[] datastores) {
        this.datastores = datastores;
    }

    

   
    
}
