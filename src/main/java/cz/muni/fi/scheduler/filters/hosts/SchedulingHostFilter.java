/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.muni.fi.scheduler.filters.hosts;

import cz.muni.fi.scheduler.filters.hosts.strategies.ISchedulingHostFilterStrategy;
import cz.muni.fi.scheduler.filters.hosts.strategies.IHostFilterStrategy;
import cz.muni.fi.scheduler.SchedulerData;
import cz.muni.fi.scheduler.elementpools.IHostPool;
import cz.muni.fi.scheduler.resources.HostElement;
import cz.muni.fi.scheduler.resources.VmElement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Gabriela Podolnikova
 */
public class SchedulingHostFilter implements IHostFilter {

    /**
     * The list of filters to be used for matching the host for a virtual machine.
     */
    private List<IHostFilterStrategy> hostFilters;
    
    private List<ISchedulingHostFilterStrategy> schedulingHostFilters;
    
    private IHostPool hostPool;
    
    private SchedulerData schedulerData;

    public SchedulingHostFilter(List<IHostFilterStrategy> hostFilters, List<ISchedulingHostFilterStrategy> schedulingHostFilters, IHostPool hostPool, SchedulerData schedulerData) {
        this.hostFilters = hostFilters;
        this.schedulingHostFilters = schedulingHostFilters;
        this.hostPool = hostPool;
        this.schedulerData = schedulerData;
    }
    
    /**
     * Filters hosts that are authorized for the specified vm.
     * Calls the filters for each host.
     * @param authorizedHosts the hosts to be tested.
     * @param vm the virtual machine to be tested
     * @return the list of filtered hosts
     */
    @Override
    public List<HostElement> getFilteredHosts(List<Integer> authorizedHosts, VmElement vm) {
        List<HostElement> filteredHosts = new ArrayList<>();
        for (Integer hostId : authorizedHosts) {
            HostElement h = hostPool.getHost(hostId);
            boolean matched = isSuitableHost(h, vm);
            if (matched) {
                filteredHosts.add(h);
            }
        }
        return filteredHosts;
    }
    
    /**
     * Goes through all filters and calls the test if the vm and host matches by the specified criteria in the filter.
     * @param filters filters to be used
     * @param h the host to be tested
     * @param vm the virtual machine to be tested
     * @return true if the host and vm match, false othewise
     */
    public boolean isSuitableHost(HostElement h, VmElement vm) {
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
