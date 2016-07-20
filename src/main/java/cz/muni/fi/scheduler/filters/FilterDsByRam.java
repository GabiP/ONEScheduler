package cz.muni.fi.scheduler.filters;

import cz.muni.fi.scheduler.Scheduler;
import cz.muni.fi.scheduler.elementpools.IClusterPool;
import cz.muni.fi.scheduler.elementpools.IDatastorePool;
import cz.muni.fi.scheduler.resources.DatastoreElement;
import cz.muni.fi.scheduler.resources.HostElement;
import cz.muni.fi.scheduler.resources.VmElement;
import java.util.Map;

/**
 * Tests whether current host has enough free space(mb) in datastores to host
 * the specified vm disks. Simplified version: firstly it adds up the sizes(mb)
 * of vm's disks. Then this value is checked on cluster's datastores.
 * TODO: It can divide the sizes of disks and try if it will fit somehow on the available
 * datastores.
 *
 * @author Gabriela Podolnikova
 */
public class FilterDsByRam implements IFilter {

    @Override
    public boolean test(VmElement vm, HostElement host, IClusterPool clusterPool, IDatastorePool dsPool, Scheduler scheduler) {
        Map<HostElement, Integer> dsMemUsages = scheduler.getMemoryUsagesDs();
        int sizeValue = vm.getDiskSizes();
        System.out.println("Filtering Host's ds by free memory: " + dsMemUsages.get(host) + " checking " + sizeValue);
        return (dsMemUsages.get(host) >= sizeValue);
        /*DatastoreElement ds = host.getHostsFreeDatastore(sizeValue, clusterPool,dsPool, dsMemUsages);
        if (null == ds) {
            System.out.println("Datastore does not have enough capacity to host the vm");
            return false;
        }
        return true;*/
    }
}
