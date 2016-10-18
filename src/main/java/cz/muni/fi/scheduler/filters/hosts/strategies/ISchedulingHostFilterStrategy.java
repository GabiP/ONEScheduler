package cz.muni.fi.scheduler.filters.hosts.strategies;

import cz.muni.fi.scheduler.SchedulerData;
import cz.muni.fi.scheduler.resources.HostElement;
import cz.muni.fi.scheduler.resources.VmElement;

/**
 * Filters are used for filtering active hosts in the system.
 * It matches the given Virtual machine and host, whether it meets the desired criteria.
 * For each criteria we create one Filter class by impelenting this interface.
 * 
 * @author Gabriela Podolnikova
 */
public interface ISchedulingHostFilterStrategy {
    
    public boolean test(VmElement vm, HostElement host, SchedulerData schedulerData);
}
