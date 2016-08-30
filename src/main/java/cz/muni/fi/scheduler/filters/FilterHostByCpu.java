package cz.muni.fi.scheduler.filters;

import cz.muni.fi.scheduler.SchedulerData;
import cz.muni.fi.scheduler.resources.HostElement;
import cz.muni.fi.scheduler.resources.VmElement;
import java.util.Map;

/**
 *
 * @author Gabriela Podolnikova
 */
public class FilterHostByCpu implements IHostFilter {
    
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
        Map<HostElement, Float> cpuUsages = schedulerData.getCpuUsages();
        System.out.println("Filtering Hosts by cpu: " + host.getMax_cpu() + "-" + cpuUsages.get(host) + "=" +(host.getMax_cpu() - cpuUsages.get(host)) + ">=" + vm.getCpu());
        return ((host.getMax_cpu() - cpuUsages.get(host)) >= vm.getCpu());
    }
    
}
