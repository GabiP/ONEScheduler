/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.scheduler.filters.hosts;

import cz.muni.fi.scheduler.filters.hosts.strategies.IHostFilterStrategy;
import cz.muni.fi.scheduler.elements.HostElement;
import cz.muni.fi.scheduler.elements.VmElement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Andras Urge
 */
public class HostFilter {
    
    private List<IHostFilterStrategy> hostFilters;
            
    public HostFilter(List<IHostFilterStrategy> hostFilters) {
        this.hostFilters = hostFilters;
    }
    
    /**
     * Returns the hosts that are suitable for the specified vm.
     * 
     * @param hosts the hosts to be tested.
     * @param vm the virtual machine to be tested
     * @return the list of filtered hosts
     */
    public List<HostElement> getFilteredHosts(List<HostElement> hosts, VmElement vm) {
        List<HostElement> filteredHosts = new ArrayList<>();
        for (HostElement h : hosts) {
            boolean suitableHost = isSuitableHost(h, vm);
            if (suitableHost) {
                filteredHosts.add(h);
            }
        }
        return filteredHosts;
    }
    
    /**
     * Goes through all filters and checks if the host is suitable for the vm
     * by the specified criteria in the filters.
     * 
     * @param host the host to be tested
     * @param vm the virtual machine to be tested
     * @return true if host is suitable for vm, false otherwise
     */
    private boolean isSuitableHost(HostElement host, VmElement vm) {
        boolean suitable = true;
        for (IHostFilterStrategy filter: hostFilters) {
            suitable = suitable && filter.test(vm, host);
        }
        return suitable;
    }
}
