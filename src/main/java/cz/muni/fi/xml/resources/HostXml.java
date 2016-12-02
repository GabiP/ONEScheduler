package cz.muni.fi.xml.resources;

import cz.muni.fi.xml.resources.nodes.DatastoreNodeXml;
import cz.muni.fi.xml.resources.nodes.PciNodeXml;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.util.List;

/**
 * This class represents the Host retrived from xml.
 * @author Gabriela Podolnikova
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
    
    @JacksonXmlProperty(localName = "MAX_DISK")
    private Integer max_disk;

    @JacksonXmlProperty(localName = "MAX_MEM")
    private Integer max_mem;

    @JacksonXmlProperty(localName = "MAX_CPU")
    private Float max_cpu;

    @JacksonXmlProperty(localName = "FREE_DISK")
    private Integer free_disk;
    
    @JacksonXmlProperty(localName = "FREE_MEM")
    private Integer free_mem;

    @JacksonXmlProperty(localName = "FREE_CPU")
    private Float free_cpu;

    @JacksonXmlProperty(localName = "USED_DISK")
    private Integer used_disk;
    
    @JacksonXmlProperty(localName = "USED_MEM")
    private Integer used_mem;
    
    @JacksonXmlProperty(localName = "USED_CPU")
    private Integer used_cpu;
    
    @JacksonXmlProperty(localName = "RUNNING_VMS")
    private Integer runningVms;
    
    @JacksonXmlProperty(localName = "RESERVED_CPU")
    private Integer reservedCpu;
    
    @JacksonXmlProperty(localName = "RESERVED_MEMORY")
    private Integer reservedMemory;
    
    @JacksonXmlProperty(localName = "VMS")
    private List<Integer> vms;
        
    @JacksonXmlProperty(localName = "PCI_DEVICES")
    private List<PciNodeXml> pcis;
    
    @JacksonXmlProperty(localName = "DATASTORES")
    private List<DatastoreNodeXml> datastores;

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
        String text =  "HostXml{" + "id=" + id + ", name=" + name + ", state=" + state + ", clusterId=" + clusterId + ", disk_usage=" + disk_usage + ", mem_usage=" + mem_usage + ", cpu_usage=" + cpu_usage + ", max_disk=" + max_disk + ", max_mem=" + max_mem + ", max_cpu=" + max_cpu + ", free_disk=" + free_disk + ", free_mem=" + free_mem + ", free_cpu=" + free_cpu + ", used_disk=" + used_disk + ", used_mem=" + used_mem + ", used_cpu=" + used_cpu + ", runningVms=" + runningVms + ", reservedCpu=" + reservedCpu + ", reservedMemory=" + reservedMemory + ", vms=" + vms + ", pcis=" + pcis + ", datastores=" + datastores + '}';
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

    public List<PciNodeXml> getPcis() {
        return pcis;
    }

    public void setPcis(List<PciNodeXml> pcis) {
        this.pcis = pcis;
    }

    public List<DatastoreNodeXml> getDatastores() {
        return datastores;
    }

    public void setDatastores(List<DatastoreNodeXml> datastores) {
        this.datastores = datastores;
    }

    /**
     * @return the vms
     */
    public List<Integer> getVms() {
        return vms;
    }

    /**
     * @param vms the vms to set
     */
    public void setVms(List<Integer> vms) {
        this.vms = vms;
    }

    

   
    
}
