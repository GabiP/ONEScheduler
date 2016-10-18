package cz.muni.fi.scheduler;

import cz.muni.fi.scheduler.elementpools.IClusterPool;
import cz.muni.fi.scheduler.elementpools.IDatastorePool;
import cz.muni.fi.scheduler.elementpools.IHostPool;
import cz.muni.fi.scheduler.elementpools.IVmPool;
import cz.muni.fi.scheduler.resources.DatastoreElement;
import cz.muni.fi.scheduler.resources.HostElement;
import cz.muni.fi.scheduler.resources.VmElement;
import cz.muni.fi.scheduler.resources.nodes.DatastoreNode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class stores the data for the scheduler entity.
 * This data can be used for the calculation of metrics, filtering and scheduling algorithms.
 * 
 * @author Gabriela Podolnikova
 */
public class SchedulerData {
    
    private IHostPool hostPool;
    
    private IVmPool vmPool;    
    
    private IDatastorePool dsPool;
    
    /**
     * This map is used for computing the cpu usages.
     * Every time we match a host with a virtual machine the cpu usage needs to be increased.
     * We are storing the used space.
     */
    private Map<HostElement, Float> cpuUsages;
    
    /**
     * This map is used for computing the memory usages.
     * Every time we match a host with a virtual machine the memory usage needs to be increased.
     * We are storing the used space.
     */
    private Map<HostElement, Integer> memoryUsages;
    
    /**
     * This map is used for computing the free spaces in datastore nodes found on hosts.
     * Every time we match a host with a virtual machine the free space on hosts datastores needs to be decreased.
     * We are storing the free space.
     */
    //private Map<HostElement, List<Integer>> datastoreNodeStorageCapacity;
    private Map<HostElement, List<DatastoreNode>> datastoreNodeStorageCapacity;
    
    /**
     * This map is used for computing the free space on datastore directly.
     * Every time we match a host with a virtual machine the free space on hosts datastores needs to be decreased.
     * We are storing the free space.
     */
    private Map<DatastoreElement, Integer> datastoreStorageCapacity;
    
    /**
     * This map is used for counting running/deployed vms on host.
     * Every time we match a host with a virtual machine the count uf running virtual machines on host needs to be increased.
     */
    private Map<HostElement, Integer> runningVms;

    public SchedulerData(IHostPool hostPool, IVmPool vmPool, IDatastorePool dsPool) {
        this.hostPool = hostPool;
        this.vmPool = vmPool;
        this.dsPool = dsPool;
        this.cpuUsages = initializeHostsCpuCapacity(hostPool.getActiveHosts());
        this.memoryUsages = initializeHostMemoryCapacity(hostPool.getActiveHosts());
        this.datastoreNodeStorageCapacity = initializeDatastoreNodeStorageCapacity(hostPool.getActiveHosts());
        this.datastoreStorageCapacity = initializeDatastoreStorageCapacity(dsPool.getSystemDs());
        this.runningVms = initializeHostRunningVms(hostPool.getActiveHosts());
    }
    
    public Map<HostElement, Float> initializeHostsCpuCapacity(List<HostElement> hosts) {
        Map<HostElement, Float> usages = new HashMap<>();
        for (HostElement host: hosts) {
            usages.put(host, host.getCpu_usage());
        }
        return usages;
    }
    
    public Map<HostElement, Integer> initializeHostMemoryCapacity(List<HostElement> hosts) {
        Map<HostElement, Integer> usages = new HashMap<>();
        for (HostElement host: hosts) {
            usages.put(host, host.getMem_usage());
        }
        return usages;
    }
    
    /*public Map<HostElement, List<Integer>> initializeDatastoreNodeStorageCapacity(List<HostElement> hosts) {
        Map<HostElement, List<Integer>> usages = new HashMap<>();
        for (HostElement h: hosts) {
            List<DatastoreNode> datastores = h.getDatastores();
            for (DatastoreNode dsnode: datastores) {
                usages = putValueToUsage(usages, h, dsnode.getFree_mb());
            }
        }
        return usages;
    }*/
    
    public Map<HostElement, List<DatastoreNode>> initializeDatastoreNodeStorageCapacity(List<HostElement> hosts) {
        Map<HostElement, List<DatastoreNode>> usages = new HashMap<>();
        for (HostElement h: hosts) {
            List<DatastoreNode> datastores = onlySystemDsNode(h.getDatastores(), dsPool);
            usages.put(h, datastores);
        }
        return usages;
    }
    
    private List<DatastoreNode> onlySystemDsNode(List<DatastoreNode> datastores, IDatastorePool dsPool) {
        List<DatastoreNode> systemDsNodes = new ArrayList<>();
        for (DatastoreNode dsNode: datastores) {
            DatastoreElement ds = dsPool.getDatastore(dsNode.getId_ds());
            if (ds.isSystem()) {
                systemDsNodes.add(dsNode);
            }
        }
        return systemDsNodes;
    }
    
    /*public Map<HostElement, List<Integer>> putValueToUsage(Map<HostElement, List<Integer>> map, HostElement host, Integer usage) {
        if (map.containsKey(host)) {
            map.get(host).add(usage);
        } else {
            List<Integer> values = new ArrayList<>();
            values.add(usage);
            map.put(host, values);
        }
        return map;
    }*/
    
    public Map<DatastoreElement, Integer> initializeDatastoreStorageCapacity(List<DatastoreElement> datastores) {
        Map<DatastoreElement, Integer> usages = new HashMap<>();
        for (DatastoreElement ds: datastores) {
            usages.put(ds, ds.getFree_mb());
        }
        return usages;
    }
    
    public Map<HostElement, Integer> initializeHostRunningVms(List<HostElement> hosts) {
        Map<HostElement, Integer> usages = new HashMap<>();
        for (HostElement h: hosts) {
            usages.put(h, h.getRunningVms());
        }
        return usages;
    }
    
    public Map<HostElement, Float> addHostCpuCapacity(HostElement host, VmElement vm) {
        cpuUsages.replace(host, cpuUsages.get(host), cpuUsages.get(host) + vm.getCpu());
        return cpuUsages;
    }
    
    public Map<HostElement, Integer> addHostMemoryCapacity(HostElement host, VmElement vm) {
        memoryUsages.replace(host, memoryUsages.get(host), memoryUsages.get(host) + vm.getMemory());
        return memoryUsages;
    }
    
    /*public Map<HostElement, List<Integer>> addDatastoreNodeStorageCapacity(HostElement host, VmElement vm, Integer index) {
        List<Integer> usagesToUpdate = datastoreNodeStorageCapacity.get(host);
        Integer use = usagesToUpdate.get(index);
        usagesToUpdate.set(index, use -vm.getDiskSizes());
        datastoreNodeStorageCapacity.replace(host, datastoreNodeStorageCapacity.get(host), usagesToUpdate);
        return datastoreNodeStorageCapacity;
    }*/
    
    public Map<HostElement, List<DatastoreNode>> addDatastoreNodeStorageCapacity(HostElement host, DatastoreNode ds, VmElement vm) {
        List<DatastoreNode> usageToUpdate = datastoreNodeStorageCapacity.get(host);
        DatastoreNode dsToUpdate = usageToUpdate.get(usageToUpdate.indexOf(ds));
        dsToUpdate.update(vm.getDiskSizes());
        return datastoreNodeStorageCapacity;
    }
    
    public Map<DatastoreElement, Integer> addDatastoreStorageCapacity(DatastoreElement ds, VmElement vm) {
        datastoreStorageCapacity.replace(ds, datastoreStorageCapacity.get(ds), datastoreStorageCapacity.get(ds) - vm.getDiskSizes());
        return datastoreStorageCapacity;
    }
    
    public Map<HostElement, Integer> addHostRunningVm(HostElement host) {
        runningVms.replace(host, runningVms.get(host), runningVms.get(host) + 1);
        return runningVms;
    }

    public IHostPool getHostPool() {
        return hostPool;
    }

    public void setHostPool(IHostPool hostPool) {
        this.hostPool = hostPool;
    }

    public IVmPool getVmPool() {
        return vmPool;
    }

    public void setVmPool(IVmPool vmPool) {
        this.vmPool = vmPool;
    }

    public IDatastorePool getDsPool() {
        return dsPool;
    }

    public void setDsPool(IDatastorePool dsPool) {
        this.dsPool = dsPool;
    }

    public Map<HostElement, Float> getCpuUsages() {
        return cpuUsages;
    }

    public void setCpuUsages(Map<HostElement, Float> cpuUsages) {
        this.cpuUsages = cpuUsages;
    }

    public Map<HostElement, Integer> getMemoryUsages() {
        return memoryUsages;
    }

    public void setMemoryUsages(Map<HostElement, Integer> memoryUsages) {
        this.memoryUsages = memoryUsages;
    }

    /*public Map<HostElement, List<Integer>> getDatastoreNodeStorageCapacity() {
        return datastoreNodeStorageCapacity;
    }

    public void setDatastoreNodeStorageCapacity(Map<HostElement, List<Integer>> datastoreNodeStorageCapacity) {
        this.datastoreNodeStorageCapacity = datastoreNodeStorageCapacity;
    }*/
    
    public Map<HostElement, List<DatastoreNode>> getDatastoreNodeStorageCapacity() {
        return datastoreNodeStorageCapacity;
    }

    public void setDatastoreNodeStorageCapacity(Map<HostElement, List<DatastoreNode>> datastoreNodeStorageCapacity) {
        this.datastoreNodeStorageCapacity = datastoreNodeStorageCapacity;
    }

    public Map<DatastoreElement, Integer> getDatastoreStorageCapacity() {
        return datastoreStorageCapacity;
    }

    public void setDatastoreStorageCapacity(Map<DatastoreElement, Integer> datastoreStorageCapacity) {
        this.datastoreStorageCapacity = datastoreStorageCapacity;
    }

    public Map<HostElement, Integer> getRunningVms() {
        return runningVms;
    }

    public void setRunningVms(Map<HostElement, Integer> runningVms) {
        this.runningVms = runningVms;
    }

}
