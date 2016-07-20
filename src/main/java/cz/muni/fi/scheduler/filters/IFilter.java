package cz.muni.fi.scheduler.filters;

import cz.muni.fi.scheduler.Scheduler;
import cz.muni.fi.scheduler.elementpools.IClusterPool;
import cz.muni.fi.scheduler.elementpools.IDatastorePool;
import cz.muni.fi.scheduler.resources.HostElement;
import cz.muni.fi.scheduler.resources.VmElement;

/**
 * Filters are used for filtering active hosts in the system.
 * It matches the given Virtual machine and host, whether it meets the desired criteria.
 * For each criteria we create one Filter class by impelenting this interface.
 * 
 * @author Gabriela Podolnikova
 */
public interface IFilter {
    
    public boolean test(VmElement vm, HostElement host, IClusterPool clusterPool, IDatastorePool dsPool, Scheduler scheduler);
}
