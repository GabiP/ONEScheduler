package cz.muni.fi.scheduler.filters.hosts.strategies;

import cz.muni.fi.scheduler.core.SchedulerData;
import cz.muni.fi.scheduler.resources.HostElement;
import cz.muni.fi.scheduler.resources.VmElement;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Gabriela Podolnikova
 */
public class FilterHostByCpu implements ISchedulingHostFilterStrategy {
    
    protected final Logger LOG = LoggerFactory.getLogger(getClass());
    
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
        Float actualCpuUsage = schedulerData.getReservedCpu(host) + host.getCpu_usage();
        LOG.info("Filtering Hosts by cpu: " + host.getMax_cpu() + "-" + actualCpuUsage + "=" +(host.getMax_cpu() - actualCpuUsage) + ">=" + vm.getCpu());
        return ((host.getMax_cpu() - actualCpuUsage) >= vm.getCpu());
    }
    
}
