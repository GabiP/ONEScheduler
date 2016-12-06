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
public class FilterHostByCpu implements ISchedulingHostFilterStrategy {
    
    protected final Logger log = LoggerFactory.getLogger(getClass());
    
    private IClusterPool clusterPool;
    
    public FilterHostByCpu(IClusterPool clusterPool) {
        this.clusterPool = clusterPool;
    }
    
    /**
     * Tests whether a VM can be hosted by the host.
     * Checks the cpu capacity on host.
     * 
     * @param vm Vm to be checked
     * @param host Host to be checked
     * @param schedulerData a scheduler data instance providing the information about current cpuUsage during scheduling.
     * @return true if the VM can be hosted by the host. False otherwise.
     */
    @Override
    public boolean test(VmElement vm, HostElement host, SchedulerData schedulerData) {
        Float reservation = getReservation(host);
        Float actualCpuUsage = schedulerData.getReservedCpu(host) + host.getCpu_usage() + reservation;
        log.info("Filtering Hosts by cpu: " + host.getMax_cpu() + "-" + actualCpuUsage + "=" +(host.getMax_cpu() - actualCpuUsage) + ">=" + vm.getCpu());
        return ((host.getMax_cpu() - actualCpuUsage) >= vm.getCpu());
    }
    
    private Float getReservation(HostElement host) {
        Float reservedCpu = host.getReservedCpu();
        ClusterElement cluster = clusterPool.getCluster(host.getClusterId());
        Float reservedClusterCpu = cluster.getReservedCpu();
        if (reservedCpu == 0) {
            log.info("Reserved cluster cpu: " + reservedClusterCpu);
            return reservedClusterCpu;
        } else {
            log.info("Reserved host cpu: " + reservedCpu);
            return reservedCpu;
        }
    }
}
