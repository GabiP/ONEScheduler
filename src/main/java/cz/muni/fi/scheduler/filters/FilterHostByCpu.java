package cz.muni.fi.scheduler.filters;

import cz.muni.fi.scheduler.Scheduler;
import cz.muni.fi.scheduler.elementpools.IClusterPool;
import cz.muni.fi.scheduler.elementpools.IDatastorePool;
import cz.muni.fi.scheduler.resources.DatastoreElement;
import cz.muni.fi.scheduler.resources.HostElement;
import cz.muni.fi.scheduler.resources.VmElement;
import java.util.Map;

/**
 *
 * @author Gabriela Podolnikova
 */
public class FilterHostByCpu implements IFilter {
    
    /**
     * Tests whether a VM can be hosted by the host.
     * Checks the cpu capacity on host.
     * 
     * @param vm Vm to be checked
     * @param host Host to be checked
     * @param clusterPool
     * @param dsPool
     * @param scheduler a scheduler instance providing the information about current cpuUsage during scheduling.
     * @return true if the VM can be hosted by the host. False otherwise.
     */
    @Override
    public boolean test(VmElement vm, HostElement host, IClusterPool clusterPool, IDatastorePool dsPool, Scheduler scheduler) {
        Map<HostElement, Float> cpuUsages = scheduler.getCpuUsages();
        System.out.println("Filtering Hosts by cpu: " + host.getMax_cpu() + "-" + cpuUsages.get(host) + "=" +(host.getMax_cpu() - cpuUsages.get(host)) + ">=" + vm.getCpu());
        return ((host.getMax_cpu() - cpuUsages.get(host)) >= vm.getCpu());
    }
    
}
