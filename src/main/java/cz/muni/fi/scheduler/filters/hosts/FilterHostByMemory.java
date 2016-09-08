package cz.muni.fi.scheduler.filters.hosts;

import cz.muni.fi.scheduler.SchedulerData;
import cz.muni.fi.scheduler.resources.HostElement;
import cz.muni.fi.scheduler.resources.VmElement;
import java.util.Map;

/**
 *
 * @author Gabriela Podolnikova
 */
public class FilterHostByMemory implements IHostFilter {
    
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
        Map<HostElement, Integer> memoryUsages = schedulerData.getMemoryUsages();
        System.out.println("Filtering Hosts by memory: " + host.getMax_mem() + "-" + memoryUsages.get(host) + "=" +(host.getMax_mem() - memoryUsages.get(host)) + ">=" + vm.getMemory());
        return ((host.getMax_mem() - memoryUsages.get(host)) >= vm.getMemory());
    }
}
