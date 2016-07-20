package cz.muni.fi.scheduler.filters;

import cz.muni.fi.scheduler.Scheduler;
import cz.muni.fi.scheduler.elementpools.IClusterPool;
import cz.muni.fi.scheduler.elementpools.IDatastorePool;
import cz.muni.fi.scheduler.resources.HostElement;
import cz.muni.fi.scheduler.resources.VmElement;
import java.util.Map;

/**
 *
 * @author Gabriela Podolnikova
 */
public class FilterHostByMemory implements IFilter {
    
    /**
     * Tests whether a VM can be hosted by the host.
     * Checks the memory capacity on host.
     * 
     * @param vm Vm to be checked
     * @param host Host to be checked
     * @param clusterPool
     * @param dsPool
     * @param scheduler a scheduler instance providing the information about current memoryUsage during scheduling.
     * @return true if the VM can be hosted by the host. False otherwise.
     */
    @Override
    public boolean test(VmElement vm, HostElement host, IClusterPool clusterPool, IDatastorePool dsPool, Scheduler scheduler) {
        Map<HostElement, Integer> memoryUsages = scheduler.getMemoryUsages();
        System.out.println("Filtering Hosts by memory: " + host.getMax_mem() + "-" + memoryUsages.get(host) + "=" +(host.getMax_mem() - memoryUsages.get(host)) + ">=" + vm.getMemory());
        return ((host.getMax_mem() - memoryUsages.get(host)) >= vm.getMemory());
    }
}
