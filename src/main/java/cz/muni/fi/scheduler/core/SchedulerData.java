package cz.muni.fi.scheduler.core;

import cz.muni.fi.scheduler.resources.DatastoreElement;
import cz.muni.fi.scheduler.resources.HostElement;
import cz.muni.fi.scheduler.resources.VmElement;
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
     * This map is used for counting running/deployed vms on host.
     * Every time we match a host with a virtual machine the count uf running virtual machines on host needs to be increased.
     */
    private Map<HostElement, Integer> runningVms;
    
    /**
     * This map is used for storing the reserved space on datastores.
     */
    private Map<DatastoreElement, Integer> reservedDsStorage;
    
    /**
     * This map is used for computing the free spaces in datastore nodes found on hosts.
     * Every time we match a host with a virtual machine the free space on hosts datastores needs to be decreased.
     * We are storing the free space.
     */
    private Map<HostElement, Map<Integer, Integer>> reservedDsNodeStorage;

    public SchedulerData() {
        cpuReservation = new HashMap<>();
        memoryReservation = new HashMap<>();
        runningVms = new HashMap<>();
        reservedDsStorage = new HashMap<>();
        reservedDsNodeStorage = new HashMap<>();
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
    
    public Map<DatastoreElement, Integer> reserveDatastoreStorage(DatastoreElement ds, VmElement vm) {
        if (reservedDsStorage.containsKey(ds)) {
            reservedDsStorage.replace(ds, reservedDsStorage.get(ds), reservedDsStorage.get(ds) + vm.getDiskSizes());
        } else {
            reservedDsStorage.put(ds, vm.getDiskSizes());
        }
        return reservedDsStorage;
    }
    
    public Map<HostElement, Map<Integer, Integer>> reserveDatastoreNodeStorage(HostElement host, DatastoreElement ds, VmElement vm) {
        if (reservedDsNodeStorage.containsKey(host)) {
            Map<Integer, Integer> hostsNodesReservations = reservedDsNodeStorage.get(host);
            if (hostsNodesReservations.containsKey(ds.getId())) {
                hostsNodesReservations.replace(ds.getId(), hostsNodesReservations.get(ds.getId()), hostsNodesReservations.get(ds.getId()) + vm.getDiskSizes());
            } else {
                hostsNodesReservations.put(ds.getId(), vm.getDiskSizes());
            }
        } else {
            reservedDsNodeStorage.put(host, new HashMap<>());
            reservedDsNodeStorage.get(host).put(ds.getId(), vm.getDiskSizes());
        }
        return reservedDsNodeStorage;
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
        return 0.00f;
    }
    
    public Integer getReservedRunningVms(HostElement host) {
        if (runningVms.containsKey(host)) {
            return runningVms.get(host);
        }
        return 0;
    }
    
    public Integer getReservedStorage(DatastoreElement ds) {
        if (reservedDsStorage.containsKey(ds)) {
            return reservedDsStorage.get(ds);
        }
        return 0;
    }
    
    public Integer getReservedStorage(HostElement host, DatastoreElement ds) {
        if (reservedDsNodeStorage.containsKey(host)) {
            Map<Integer, Integer> hostsNodesReservations = reservedDsNodeStorage.get(host);
            if (hostsNodesReservations.containsKey(ds.getId())) {
                return hostsNodesReservations.get(ds.getId());
            }
        }
        return 0;
    }
    
    public Map<HostElement, Integer> getActualRunningVms(List<HostElement> hosts) {
        Map<HostElement, Integer> actualRunningVms = new HashMap<>();
        Integer vmsOnHost;
        for (HostElement host: hosts) {
            if (!runningVms.containsKey(host)) {
                vmsOnHost = 0;
            } else {
                vmsOnHost = runningVms.get(host);
            }
            Integer numberofVms = vmsOnHost + host.getRunningVms();
            actualRunningVms.put(host, numberofVms);
        }
        return actualRunningVms;
    }
}
