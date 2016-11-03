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
    private Map<DatastoreElement, Integer> reservedStorage;

    public SchedulerData() {
        cpuReservation = new HashMap<>();
        memoryReservation = new HashMap<>();
        runningVms = new HashMap<>();
        reservedStorage = new HashMap<>();
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
        if (reservedStorage.containsKey(ds)) {
            reservedStorage.replace(ds, reservedStorage.get(ds), reservedStorage.get(ds) + vm.getDiskSizes());
        } else {
            reservedStorage.put(ds, vm.getDiskSizes());
        }
        return reservedStorage;
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
    
    public Integer getReservedStorage(DatastoreElement ds) {
        if (reservedStorage.containsKey(ds)) {
            return reservedStorage.get(ds);
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
