package cz.muni.fi.scheduler.resources;

import cz.muni.fi.scheduler.resources.nodes.DiskNode;
import cz.muni.fi.scheduler.resources.nodes.PciNode;
import cz.muni.fi.scheduler.resources.nodes.DatastoreNode;
import cz.muni.fi.scheduler.elementpools.IClusterPool;
import cz.muni.fi.scheduler.elementpools.IDatastorePool;
import java.util.List;
import java.util.Objects;

/**
 * This class represents an OpenNebula Host
 *
 * @author Gabriela Podolnikova
 */
public class HostElement {

    private Integer id;

    private String name;

    private Integer state;

    private Integer clusterId;
    
    private Integer disk_usage;
    
    private Integer mem_usage;
    
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
        
    private List<PciNode> pcis;
        
    private List<DatastoreNode> datastores;
        
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

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getClusterId() {
        return clusterId;
    }

    public void setClusterId(Integer clusterId) {
        this.clusterId = clusterId;
    }


    @Override
    public String toString() {
        return "Host{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", state=" + state +
                ", clusterId=" + clusterId +
                ", pcis=" + pcis +
                ", datastores=" + datastores +
                '}';
    }

    
    /**
     * Tests whether a VM can be hosted by the host.
     * Checks the memory and capacity on host.
     * 
     * @param vm virtual machine to be tested
     * @return true if VM can be hosted, false otherwise
     */
    public boolean testCapacity(VmElement vm) {
        System.out.println("testCapacity - cpu:" + max_cpu + " - " + cpu_usage + " = " + (max_cpu - cpu_usage) + " =? " + free_cpu + " vm cpu: " + vm.getCpu());
        System.out.println("testCapacity - mem:" + max_mem + " - " + mem_usage + " = " + (max_mem - mem_usage) + " =? " + free_mem + " vm mem: " + vm.getMemory());
        return ((max_cpu - cpu_usage) >= vm.getCpu()) && ((max_mem - mem_usage) >= vm.getMemory());
    }
    
    /**
     * Increases cpu and memory on current host. And decreases free cpu and memory.
     * @param vm virtual machine with information for increasing the capacity
     */
    public void addCapacity(VmElement vm) {
         cpu_usage += vm.getCpu();
         mem_usage += vm.getMemory();
         free_cpu -= vm.getCpu();
         free_mem -= vm.getMemory();
    }
    
    /**
     * Decreases cpu and memory on current host. And increases free cpu and memory.
     * @param vm virtual machine with information for increasing the capacity
     */
    public void delCapacity(VmElement vm) {
         cpu_usage -= vm.getCpu();
         mem_usage -= vm.getMemory();
         free_cpu += vm.getCpu();
         free_mem += vm.getMemory();
    }
    
    /**
     * Tests whether current host has enough free space(mb) in datastores to host the specified vm disks.
     * Simplified version: firstly it adds up the sizes(mb) of vm's disks. Then this value is checked on cluster's datastores.
     * TODO: It can divide the sizes of disks and try if it will fit somehow on the available datastores.
     * @param vm to be tested
     * @param clusterPool all clusters to find the host cluster
     * @param dsPool all datastore to find the ds to be checked
     * @return true if the vm fits, false otherwise
     */
    public boolean testDs(VmElement vm, IClusterPool clusterPool, IDatastorePool dsPool) {
        boolean fits = false;
        ClusterElement cluster = clusterPool.getCluster(this.clusterId);
        List<Integer> datastoresIds = cluster.getDatastores();
        List<DiskNode> disks = vm.getDisks();
        int sizeValue = 0;
        for (DiskNode disk : disks) {
            sizeValue += disk.getSize();
        }
        for (Integer dsId : datastoresIds) {
            DatastoreElement ds = dsPool.getDatastore(dsId);
            if (ds.getFree_mb() > sizeValue) {
                fits = true;
                addDsCapacity(sizeValue, ds);
                return fits;
            }
        }
        if (fits == false) {
            System.out.println("Datastore does not have enough capacity to host the vm");
        }
        return fits;
    }
    
    public void addDsCapacity(int sizeValue, DatastoreElement ds) {
        ds.addUsedMb(sizeValue);
    }
    
    /**
     * Checks whether the host has the specified pci that the vm requires.
     * Note: the hosts in OpenNebula must be configured, that this functionality works.
     * @param vm the vm to be checked
     * @return true if vm can be hosted, false otherwise
     */
    public boolean checkPci(VmElement vm) {
        boolean pciFits = false;
        if (pcis.isEmpty() && vm.getPcis().isEmpty()) {
            return true;
        }
        if (!pcis.isEmpty() && vm.getPcis().isEmpty()) {
            return true;
        }
        if (pcis.isEmpty() && !vm.getPcis().isEmpty()) {
            return false;
        }
        for (PciNode pci: pcis) {
            for (PciNode pciVm: vm.getPcis()) {
                if ((pci.getPci_class().equals(pciVm.getPci_class())) && (pci.getDevice().equals(pciVm.getDevice())) && (pci.getVendor().equals(pciVm.getVendor()))) {
                    pciFits = true;
                }
            }
        }
        return pciFits;
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
    
    /**
     * @return the pcis
     */
    public List<PciNode> getPcis() {
        return pcis;
    }

    public void setPcis(List<PciNode> pcis) {
        this.pcis = pcis;
    }
    
    /**
     * @return the datastores
     */
    public List<DatastoreNode> getDatastores() {
        return datastores;
    }  

    public void setDatastores(List<DatastoreNode> datastores) {
        this.datastores = datastores;
    }
    
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 47 * hash + Objects.hashCode(this.getId());
        hash = 47 * hash + Objects.hashCode(this.getName());
        hash = 47 * hash + Objects.hashCode(this.getState());
        hash = 47 * hash + Objects.hashCode(this.getClusterId());
        return hash;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof HostElement)) {
            return false;
        }
        final HostElement other = (HostElement) obj;
        if (!Objects.equals(this.getId(), other.getId())) {
            return false;
        }
        if (!Objects.equals(this.getName(), other.getName())) {
            return false;
        }
        if (!Objects.equals(this.getState(), other.getState())) {
            return false;
        }
        if (!Objects.equals(this.getClusterId(), other.getClusterId())) {
            return false;
        }
        return true;
    }
}
