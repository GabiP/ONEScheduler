package cz.muni.fi.scheduler.filters.hosts.strategies;

import cz.muni.fi.scheduler.core.SchedulerData;
import cz.muni.fi.scheduler.elementpools.IClusterPool;
import cz.muni.fi.scheduler.resources.ClusterElement;
import cz.muni.fi.scheduler.resources.HostElement;
import cz.muni.fi.scheduler.resources.VmElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Gabriela Podolnikova
 */
public class FilterHostByMemory implements ISchedulingHostFilterStrategy {
    
    protected final Logger log = LoggerFactory.getLogger(getClass());
    
    private IClusterPool clusterPool;
    
    public FilterHostByMemory(IClusterPool clusterPool) {
        this.clusterPool = clusterPool;
    }
    /**
     * Tests whether a VM can be hosted by the host.
     * Checks the memory capacity on host.
     * 
     * @param vm Vm to be checked
     * @param host Host to be checked
     * @param schedulerData a scheduler data instance providing the information about current memoryUsage during scheduling.
     * @return true if the VM can be hosted by the host. False otherwise.
     */
    @Override
    public boolean test(VmElement vm, HostElement host, SchedulerData schedulerData) {
        Integer reservation = getReservation(host);
        Integer actualMemoryUsage = schedulerData.getReservedMemory(host) + host.getMem_usage() + reservation;
        log.info("Filtering Hosts by memory: " + host.getMax_mem() + "-" + actualMemoryUsage + "=" +(host.getMax_mem() - actualMemoryUsage) + ">=" + vm.getMemory());
        return ((host.getMax_mem() - actualMemoryUsage) >= vm.getMemory());
    }
    
    private Integer getReservation(HostElement host) {
        Integer reservedMemory = host.getReservedMemory();
        ClusterElement cluster = clusterPool.getCluster(host.getClusterId());
        Integer reservedClusterMemory = cluster.getReservedMemory();
        if (reservedMemory == 0) {
            log.info("Reserved cluster memory: " + reservedClusterMemory);
            return reservedClusterMemory;
        } else {
            log.info("Reserved host memory: " + reservedMemory);
            return reservedMemory;
        }
    }
}
