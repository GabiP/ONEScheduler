/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.pools;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

/**
 *
 * @author Gabriela Podolnikova
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class VmQuota {
    
    @JacksonXmlProperty(localName = "CPU")
    private String cpu;
    
    @JacksonXmlProperty(localName = "CPU_USED")
    private String cpu_used;
    
    @JacksonXmlProperty(localName = "MEMORY")
    private String memory;
    
    @JacksonXmlProperty(localName = "MEMORY_USED")
    private String memory_used;
    
    @JacksonXmlProperty(localName = "VMS")
    private String vms;
    
    @JacksonXmlProperty(localName = "VMS_USED")
    private String vms_used;
    
    @JacksonXmlProperty(localName = "VOLATILE_SIZE")
    private String volatile_size;
    
    @JacksonXmlProperty(localName = "VOLATILE_SIZE_USED")
    private String volatile_size_used;

    /**
     * @return the cpu
     */
    public String getCpu() {
        return cpu;
    }

    /**
     * @param cpu the cpu to set
     */
    public void setCpu(String cpu) {
        this.cpu = cpu;
    }

    /**
     * @return the cpu_used
     */
    public String getCpu_used() {
        return cpu_used;
    }

    /**
     * @param cpu_used the cpu_used to set
     */
    public void setCpu_used(String cpu_used) {
        this.cpu_used = cpu_used;
    }

    /**
     * @return the memory
     */
    public String getMemory() {
        return memory;
    }

    /**
     * @param memory the memory to set
     */
    public void setMemory(String memory) {
        this.memory = memory;
    }

    /**
     * @return the memory_used
     */
    public String getMemory_used() {
        return memory_used;
    }

    /**
     * @param memory_used the memory_used to set
     */
    public void setMemory_used(String memory_used) {
        this.memory_used = memory_used;
    }

    /**
     * @return the vms
     */
    public String getVms() {
        return vms;
    }

    /**
     * @param vms the vms to set
     */
    public void setVms(String vms) {
        this.vms = vms;
    }

    /**
     * @return the vms_used
     */
    public String getVms_used() {
        return vms_used;
    }

    /**
     * @param vms_used the vms_used to set
     */
    public void setVms_used(String vms_used) {
        this.vms_used = vms_used;
    }

    /**
     * @return the volatile_size
     */
    public String getVolatile_size() {
        return volatile_size;
    }

    /**
     * @param volatile_size the volatile_size to set
     */
    public void setVolatile_size(String volatile_size) {
        this.volatile_size = volatile_size;
    }

    /**
     * @return the volatile_size_used
     */
    public String getVolatile_size_used() {
        return volatile_size_used;
    }

    /**
     * @param volatile_size_used the volatile_size_used to set
     */
    public void setVolatile_size_used(String volatile_size_used) {
        this.volatile_size_used = volatile_size_used;
    }
    
}
