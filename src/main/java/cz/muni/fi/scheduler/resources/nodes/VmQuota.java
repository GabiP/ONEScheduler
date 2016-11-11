package cz.muni.fi.scheduler.resources.nodes;

import org.opennebula.client.PoolElement;

/**
 *
 * @author Gabriela Podolnikova
 */
public class VmQuota extends AbstractNode {
    
    private Float cpu;
    
    private Float cpuUsed;
    
    private Integer memory;
    
    private Integer memoryUsed;
    
    private Integer systemDiskSize;
    
    private Integer systemDiskSizeUsed;
    
    private Integer vms;
    
    private Integer vmsUsed;

    @Override
    public void load(PoolElement user, String xpathExpr) {
        cpu = Float.parseFloat(user.xpath(xpathExpr + "/CPU"));
        cpuUsed = Float.parseFloat(user.xpath(xpathExpr + "/CPU_USED"));
        memory = Integer.parseInt(user.xpath(xpathExpr + "/MEMORY"));
        memoryUsed = Integer.parseInt(user.xpath(xpathExpr + "/MEMORY_USED"));
        systemDiskSize = Integer.parseInt(user.xpath(xpathExpr + "/SYSTEM_DISK_SIZE"));
        systemDiskSizeUsed = Integer.parseInt(user.xpath(xpathExpr + "/SYSTEM_DISK_SIZE_USED"));
        vms = Integer.parseInt(user.xpath(xpathExpr + "/VMS"));
        vmsUsed = Integer.parseInt(user.xpath(xpathExpr + "/VMS_USED"));
    }

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

    @Override
    public String toString() {
        return "VmQuota{" + "cpu=" + cpu + ", cpuUsed=" + cpuUsed + ", memory=" + memory + ", memoryUsed=" + memoryUsed + ", systemDiskSize=" + systemDiskSize + ", systemDiskSizeUsed=" + systemDiskSizeUsed + ", vms=" + vms + ", vmsUsed=" + vmsUsed + '}';
    }
    
}
