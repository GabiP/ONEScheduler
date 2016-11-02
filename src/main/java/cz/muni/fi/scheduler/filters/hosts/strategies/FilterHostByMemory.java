package cz.muni.fi.scheduler.filters.hosts.strategies;

import cz.muni.fi.scheduler.SchedulerData;
import cz.muni.fi.scheduler.resources.HostElement;
import cz.muni.fi.scheduler.resources.VmElement;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Gabriela Podolnikova
 */
public class FilterHostByMemory implements ISchedulingHostFilterStrategy {
    
    protected final Logger LOG = LoggerFactory.getLogger(getClass());
    
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
        Integer actualMemoryUsage = schedulerData.getReservedMemory(host) + host.getMem_usage();
        LOG.info("Filtering Hosts by memory: " + host.getMax_mem() + "-" + actualMemoryUsage + "=" +(host.getMax_mem() - actualMemoryUsage) + ">=" + vm.getMemory());
        return ((host.getMax_mem() - actualMemoryUsage) >= vm.getMemory());
    }
}
