package cz.muni.fi.scheduler.filters.hosts;

import cz.muni.fi.scheduler.filters.hosts.strategies.ISchedulingHostFilterStrategy;
import cz.muni.fi.scheduler.filters.hosts.strategies.IHostFilterStrategy;
import cz.muni.fi.scheduler.core.SchedulerData;
import cz.muni.fi.scheduler.elements.HostElement;
import cz.muni.fi.scheduler.elements.VmElement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Gabriela Podolnikova
 */
public class SchedulingHostFilter {

    /**
     * The list of filters to be used for matching the host for a virtual machine.
     */
    private List<IHostFilterStrategy> hostFilters;
    
    private List<ISchedulingHostFilterStrategy> schedulingHostFilters;

    public SchedulingHostFilter(List<IHostFilterStrategy> hostFilters, List<ISchedulingHostFilterStrategy> schedulingHostFilters) {
        this.hostFilters = hostFilters;
        this.schedulingHostFilters = schedulingHostFilters;
    }
    
    /**
     * Filters hosts that are authorized for the specified vm.
     * Calls the filters for each host.
     * @param hosts Hosts to be filtered
     * @param vm the virtual machine to be tested
     * @param schedulerData cached data
     * @return the list of filtered hosts
     */
    public List<HostElement> getFilteredHosts(List<HostElement> hosts, VmElement vm, SchedulerData schedulerData) {
        List<HostElement> filteredHosts = new ArrayList<>();
        for (HostElement h : hosts) {
            boolean matched = isSuitableHost(h, vm, schedulerData);
            if (matched) {
                filteredHosts.add(h);
            }
        }
        return filteredHosts;
    }
    
    /**
     * Goes through all filters and calls the test method.
     * @param h the host to be tested
     * @param vm the virtual machine to be tested
     * @param schedulerData ached data
     * @return true if the host and vm match, false otherwise
     */
    public boolean isSuitableHost(HostElement h, VmElement vm, SchedulerData schedulerData) {
         boolean result = true;
         for (IHostFilterStrategy filter: hostFilters) {
             result = result && filter.test(vm, h);
         }
         for (ISchedulingHostFilterStrategy filter: schedulingHostFilters) {
             result = result && filter.test(vm, h, schedulerData);
         }
         return result;
     }
}
