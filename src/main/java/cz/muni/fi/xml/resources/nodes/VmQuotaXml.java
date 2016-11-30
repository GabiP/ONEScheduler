package cz.muni.fi.xml.resources.nodes;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

/**
 *
 * @author Gabriela Podolnikova
 */
@JacksonXmlRootElement(localName = "VM")
public class VmQuotaXml {
    
    @JacksonXmlProperty(localName = "CPU")
    private Float cpu;
    
    @JacksonXmlProperty(localName = "CPU_USED")
    private Float cpuUsed;
    
    @JacksonXmlProperty(localName = "MEMORY")
    private Integer memory;
    
    @JacksonXmlProperty(localName = "MEMORY_USED")
    private Integer memoryUsed;
    
    @JacksonXmlProperty(localName = "SYSTEM_DISK_SIZE")
    private Integer systemDiskSize;
    
    @JacksonXmlProperty(localName = "SYSTEM_DISK_SIZE_USED")
    private Integer systemDiskSizeUsed;
    
    @JacksonXmlProperty(localName = "VMS")
    private Integer vms;
    
    @JacksonXmlProperty(localName = "VMS_USED")
    private Integer vmsUsed;

    public Float getCpu() {
        return cpu;
    }

    public void setCpu(Float cpu) {
        this.cpu = cpu;
    }

    public Float getCpuUsed() {
        return cpuUsed;
    }

    public void setCpuUsed(Float cpuUsed) {
        this.cpuUsed = cpuUsed;
    }

    public Integer getMemory() {
        return memory;
    }

    public void setMemory(Integer memory) {
        this.memory = memory;
    }

    public Integer getMemoryUsed() {
        return memoryUsed;
    }

    public void setMemoryUsed(Integer memoryUsed) {
        this.memoryUsed = memoryUsed;
    }

    public Integer getSystemDiskSize() {
        return systemDiskSize;
    }

    public void setSystemDiskSize(Integer systemDiskSize) {
        this.systemDiskSize = systemDiskSize;
    }

    public Integer getSystemDiskSizeUsed() {
        return systemDiskSizeUsed;
    }

    public void setSystemDiskSizeUsed(Integer systemDiskSizeUsed) {
        this.systemDiskSizeUsed = systemDiskSizeUsed;
    }

    public Integer getVms() {
        return vms;
    }

    public void setVms(Integer vms) {
        this.vms = vms;
    }

    public Integer getVmsUsed() {
        return vmsUsed;
    }

    public void setVmsUsed(Integer vmsUsed) {
        this.vmsUsed = vmsUsed;
    }
    
}
