package cz.muni.fi.scheduler.resources;

import cz.muni.fi.scheduler.resources.nodes.DiskNode;
import cz.muni.fi.scheduler.resources.nodes.PciNode;
import cz.muni.fi.scheduler.resources.nodes.DatastoreNode;
import cz.muni.fi.scheduler.elementpools.IClusterPool;
import cz.muni.fi.scheduler.elementpools.IDatastorePool;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * This class represents a Host.
 * A Host is a server that has the ability to run Virtual Machines.
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
    
    //in megabytes
    private Integer max_disk;
    
    //in megabytes
    private Integer max_mem;

    //number of cores
    private Float max_cpu;

    private Integer free_disk;
    
    private Integer free_mem;

    private Float free_cpu;

    private Integer used_disk;
    
    private Integer used_mem;
    
    private Integer used_cpu;
      
    private Integer runningVms;
    
    private Float reservedCpu;
    
    private Integer reservedMemory;
    
    private List<Integer> vms;
        
    private List<PciNode> pcis;
        
    private List<DatastoreNode> datastores;
    
    public boolean isActive() {
        return (state == 1 || state == 2 );
    }
    
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
        return "HostElement{" + "id=" + id /*+ ", name=" + name + ", state=" + state + ", clusterId=" + clusterId + ", disk_usage=" + disk_usage + ", mem_usage=" + mem_usage + ", cpu_usage=" + cpu_usage + ", max_disk=" + max_disk + ", max_mem=" + max_mem + ", max_cpu=" + max_cpu + ", free_disk=" + free_disk + ", free_mem=" + free_mem + ", free_cpu=" + free_cpu + ", used_disk=" + used_disk + ", used_mem=" + used_mem + ", used_cpu=" + used_cpu + ", runningVms=" + runningVms + ", reservedCpu=" + reservedCpu + ", reservedMemory=" + reservedMemory + ", vms=" + vms + ", pcis=" + pcis + ", datastores=" + datastores */+ '}';
    }
    
    public DatastoreNode getDatastoreNode(Integer id) {
        DatastoreNode result = null;
        for (DatastoreNode dsNode: datastores) {
            if (dsNode.getId_ds().equals(id)) {
                result = dsNode;
            }
        }
        return result;
    }
    
    /**
     * This method returns host's datastore that has enough capacity to host the VM.
     * It is necessary to check if the datastore is monitored and shared.
     * Based on that we check the values differently.
     * @param sizeValue the capacity of all VM's disks
     * @param clusterPool all clusters in the system
     * @param dsPool all datasores in the system
     * @param datastoreStorageCapacity actual datastore usage in the system during the scheduling process
     * @return the suitable datastore
     */
    public DatastoreElement getHostsSuitableDatastore(Integer sizeValue, IClusterPool clusterPool, IDatastorePool dsPool, Map<DatastoreElement, Integer> datastoreStorageCapacity) {
        ClusterElement cluster = clusterPool.getCluster(this.clusterId);
        List<Integer> datastoresIds = cluster.getDatastores();
         for (Integer dsId : datastoresIds) {
            DatastoreElement ds = dsPool.getDatastore(dsId);
            Integer free_mb = datastoreStorageCapacity.get(ds);
            if (free_mb > sizeValue) {
                return ds;
            }
        }
        return null;     
    }
    
    /**
     * 
     * @param sizeValue the capacity of all VM's disks
     * @param clusterPool all clusters in the system
     * @param dsPool all datasores in the system
     * @param datastoreNodestorageCapacity actual host's datastore usage in the system during the scheduling process
     * @return the index of the suitable datastore in the list of all datastore that this host has
     */     
    public Integer getHostsSuitableDatastoreNode(Integer sizeValue, IClusterPool clusterPool, IDatastorePool dsPool, Map<HostElement, List<Integer>> datastoreNodestorageCapacity) {
        Integer indexOfSuitableNode = null;
        for (DatastoreNode ds: datastores) {
            List<Integer> datastoresUsages = datastoreNodestorageCapacity.get(ds);
            for (int i = 0; i < datastoresUsages.size(); i ++) {
                Integer freeSpace = datastoresUsages.get(i);
                if (freeSpace > sizeValue) {
                    indexOfSuitableNode = i;
                }
            }
        }
        return indexOfSuitableNode;
    }
    
    public List<Integer> getDatastoresIds() {
        List<Integer> ids = new ArrayList<>();
        for (DatastoreNode dsNode: datastores) {
            ids.add(dsNode.getId_ds());
        }
        return ids;
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
    public Float getReservedCpu() {
        return reservedCpu;
    }

    /**
     * @param reservedCpu the reservedCpu to set
     */
    public void setReservedCpu(Float reservedCpu) {
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
