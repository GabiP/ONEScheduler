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
    private Map<HostElement, Float> cpuReservation;
    
    /**
     * This map is used for computing the memory usages.
     * Every time we match a host with a virtual machine the memory usage needs to be increased.
     * We are storing the used space.
     */
    private Map<HostElement, Integer> memoryReservation;
    
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
        this.datastoreNodeStorageCapacity = initializeDatastoreNodeStorageCapacity(hostPool.getActiveHosts());
        this.datastoreStorageCapacity = initializeDatastoreStorageCapacity(dsPool.getSystemDs());
        cpuReservation = new HashMap<>();
        memoryReservation = new HashMap<>();
        runningVms = new HashMap<>();
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
    
    public Map<HostElement, Float> reserveHostCpuCapacity(HostElement host, VmElement vm) {
        if (cpuReservation.containsKey(host)) {
            cpuReservation.replace(host, cpuReservation.get(host), cpuReservation.get(host) + vm.getCpu());
        } else {
            cpuReservation.put(host, vm.getCpu());
        }
        return cpuReservation;
    }
    
    public Map<HostElement, Integer> reserveHostMemoryCapacity(HostElement host, VmElement vm) {
        if (memoryReservation.containsKey(host)) {
            memoryReservation.replace(host, memoryReservation.get(host), memoryReservation.get(host) + vm.getMemory());
        } else {
            memoryReservation.put(host, vm.getMemory());
        }
        return memoryReservation;
    }
         
    public Map<HostElement, Integer> reserveHostRunningVm(HostElement host) {
        if (runningVms.containsKey(host)) {
            runningVms.replace(host, runningVms.get(host), runningVms.get(host) + 1);
        } else {
            runningVms.put(host, 1);
        }
        return runningVms;
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

    public Integer getReservedMemory(HostElement host) {
        if (memoryReservation.containsKey(host)) {
            return memoryReservation.get(host);
        }
        return 0;
    }
    
    public Float getReservedCpu(HostElement host) {
        if (cpuReservation.containsKey(host)) {
            return cpuReservation.get(host);
        }
        return new Float("0.00");
    }
    
    public Integer getReservedRunningVms(HostElement host) {
        if (runningVms.containsKey(host)) {
            return runningVms.get(host);
        }
        return 0;
    }
    
    public Map<HostElement, Integer> getActualRunningVms(List<HostElement> hosts) {
        Map<HostElement, Integer> actualRunningVms = new HashMap<>();
        for (HostElement host: hosts) {
            Integer numberofVms = runningVms.get(host) + host.getRunningVms();
            actualRunningVms.put(host, numberofVms);
        }
        return actualRunningVms;
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

    public Map<HostElement, List<DatastoreNode>> getDatastoreNodeStorageCapacity() {
        return datastoreNodeStorageCapacity;
    }

    public Map<DatastoreElement, Integer> getDatastoreStorageCapacity() {
        return datastoreStorageCapacity;
    }
}
