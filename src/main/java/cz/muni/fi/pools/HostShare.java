/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.pools;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.util.Map;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

/**
 *
 * @author Gabriela Podolnikova
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class HostShare {
    
    @JacksonXmlProperty(localName = "DISK_USAGE")
    private Integer disk_usage;
    
    @JacksonXmlProperty(localName = "MEM_USAGE")
    private Integer mem_usage;
    
    @JacksonXmlProperty(localName = "CPU_USAGE")
    private Integer cpu_usage;
    
    @JacksonXmlProperty(localName = "MAX_DISK")
    private Integer max_disk;
    
    @JacksonXmlProperty(localName = "MAX_MEM")
    private Integer max_mem;
    
    @JacksonXmlProperty(localName = "MAX_CPU")
    private Integer max_cpu;
    
    @JacksonXmlProperty(localName = "FREE_DISK")
    private Integer free_disk;
    
    @JacksonXmlProperty(localName = "FREE_MEM")
    private Integer free_mem;
    
    @JacksonXmlProperty(localName = "FREE_CPU")
    private Integer free_cpu;
    
    @JacksonXmlProperty(localName = "USED_DISK")
    private Integer used_disk;
    
    @JacksonXmlProperty(localName = "USED_MEM")
    private Integer used_mem;
    
    @JacksonXmlProperty(localName = "USED_CPU")
    private Integer used_cpu;
    
    @JacksonXmlProperty(localName = "RUNNING_VMS")
    private Integer running_vms;
    
    @JacksonXmlProperty(localName = "DATASTORES")
    private Map<String, HostDS> datastores;

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
    public Integer getCpu_usage() {
        return cpu_usage;
    }

    /**
     * @param cpu_usage the cpu_usage to set
     */
    public void setCpu_usage(Integer cpu_usage) {
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
    public Integer getMax_cpu() {
        return max_cpu;
    }

    /**
     * @param max_cpu the max_cpu to set
     */
    public void setMax_cpu(Integer max_cpu) {
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
    public Integer getFree_cpu() {
        return free_cpu;
    }

    /**
     * @param free_cpu the free_cpu to set
     */
    public void setFree_cpu(Integer free_cpu) {
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
     * @return the running_vms
     */
    public Integer getRunning_vms() {
        return running_vms;
    }

    /**
     * @param running_vms the running_vms to set
     */
    public void setRunning_vms(Integer running_vms) {
        this.running_vms = running_vms;
    }

    /**
     * @return the datastores
     */
    public Map<String, HostDS> getDatastores() {
        return datastores;
    }

    /**
     * @param datastores the datastores to set
     */
    public void setDatastores(Map<String, HostDS> datastores) {
        this.datastores = datastores;
    }

    
}
