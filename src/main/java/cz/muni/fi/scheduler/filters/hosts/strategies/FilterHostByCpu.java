package cz.muni.fi.scheduler.filters.hosts.strategies;

import cz.muni.fi.scheduler.core.SchedulerData;
import cz.muni.fi.scheduler.elementpools.IClusterPool;
import cz.muni.fi.scheduler.elements.ClusterElement;
import cz.muni.fi.scheduler.elements.HostElement;
import cz.muni.fi.scheduler.elements.VmElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Gabriela Podolnikova
 */
public class FilterHostByCpu implements ISchedulingHostFilterStrategy {
    
    protected final Logger log = LoggerFactory.getLogger(getClass());
    
    private IClusterPool clusterPool;
    
    private Boolean testingMode;
    
    public FilterHostByCpu(IClusterPool clusterPool, Boolean testingMode) {
        this.clusterPool = clusterPool;
        this.testingMode = testingMode;
    }
    
    /**
     * Tests whether a VM can be hosted by the host.
     * Checks the cpu capacity on host.
     * 
     * Note that this implementation uses Max cpu and Used cpu from Host.
     * The max cpu already descreases or increases its value by the reserved cpu
     * in host or cluster template.
     * 
     * For the testing mode, the reservations are calculated here.
     * 
     * @param vm Vm to be checked
     * @param host Host to be checked
     * @param schedulerData a scheduler data instance providing the information about current cpuUsage during scheduling.
     * @return true if the VM can be hosted by the host. False otherwise.
     */
    @Override
    public boolean test(VmElement vm, HostElement host, SchedulerData schedulerData) {
        Float reservation;
        if (testingMode) {
            //reservation = getReservation(host);
            reservation = 0.00f;
        } else {
            reservation = 0.00f;
        }
        Float actualCpuUsage = schedulerData.getReservedCpu(host) + host.getCpu_usage() + reservation;
        return ((host.getMax_cpu() - actualCpuUsage) >= vm.getCpu());
    }
    
    private Float getReservation(HostElement host) {
        Float reservedCpu = host.getReservedCpu();
        ClusterElement cluster = clusterPool.getCluster(host.getClusterId());
        Float reservedClusterCpu = cluster.getReservedCpu();
        if (reservedCpu == 0) {
            return reservedClusterCpu;
        } else {
            return reservedCpu;
        }
    }
}
