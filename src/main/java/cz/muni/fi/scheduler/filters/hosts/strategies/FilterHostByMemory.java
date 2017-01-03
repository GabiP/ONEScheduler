package cz.muni.fi.scheduler.filters.hosts.strategies;

import cz.muni.fi.scheduler.core.SchedulerData;
import cz.muni.fi.scheduler.elementpools.IClusterPool;
import cz.muni.fi.scheduler.elements.ClusterElement;
import cz.muni.fi.scheduler.elements.HostElement;
import cz.muni.fi.scheduler.elements.VmElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Tests whether a VM can be hosted by the host.
 * @author Gabriela Podolnikova
 */
public class FilterHostByMemory implements ISchedulingHostFilterStrategy {
    
    protected final Logger log = LoggerFactory.getLogger(getClass());
    
    private IClusterPool clusterPool;
    
    private Boolean testingMode;
    
    public FilterHostByMemory(IClusterPool clusterPool, Boolean testingMode) {
        this.clusterPool = clusterPool;
        this.testingMode = testingMode;
    }
    /**
     * Tests whether a VM can be hosted by the host.
     * Checks the memory capacity on host.
     * 
     * Note that this implementation uses Max memory and Used memory from Host.
     * The max Memory already descreases or increases its value by the reserved memory
     * in host or cluster template in OpenNebula.
     * 
     * For the testing mode, the reservations are calculated here.
     * 
     * @param vm Vm to be checked
     * @param host Host to be checked
     * @param schedulerData a scheduler data instance providing the information about current memoryUsage during scheduling.
     * @return true if the VM can be hosted by the host. False otherwise.
     */
    @Override
    public boolean test(VmElement vm, HostElement host, SchedulerData schedulerData) {
        Integer reservation;
        if (testingMode) {
            reservation = getReservation(host);
        } else {
            reservation = 0;
        }
        Integer actualMemoryUsage = schedulerData.getReservedMemory(host) + host.getMem_usage() + reservation;
        return ((host.getMax_mem() - actualMemoryUsage) >= vm.getMemory());
    }
    
    private Integer getReservation(HostElement host) {
        Integer reservedMemory = host.getReservedMemory();
        ClusterElement cluster = clusterPool.getCluster(host.getClusterId());
        Integer reservedClusterMemory = cluster.getReservedMemory();
        if (reservedMemory == 0) {
            return reservedClusterMemory;
        } else {
            return reservedMemory;
        }
    }
}
