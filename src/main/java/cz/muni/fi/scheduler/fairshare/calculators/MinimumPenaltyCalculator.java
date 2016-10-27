/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.scheduler.fairshare.calculators;

import cz.muni.fi.scheduler.elementpools.IHostPool;
import cz.muni.fi.scheduler.elementpools.IVmPool;
import cz.muni.fi.scheduler.fairshare.AbstractPriorityCalculator;
import cz.muni.fi.scheduler.fairshare.historyrecords.IUserFairshareRecordManager;
import cz.muni.fi.scheduler.fairshare.historyrecords.IVmFairshareRecordManager;
import cz.muni.fi.scheduler.filters.hosts.HostFilter;
import cz.muni.fi.scheduler.resources.HostElement;
import cz.muni.fi.scheduler.resources.VmElement;
import java.util.List;

/**
 * This class calculates the penalty of a Virtual Machine by comparing the
 * desired resources to the resources of every deployable host. The penalty
 * is calculated using the host that gives the smallest possible penalty.
 * 
 * @author Andras Urge
 */
// TODO : think of possible names
public class MinimumPenaltyCalculator extends AbstractPriorityCalculator {
    
    private HostFilter hostFilter;
    private IHostPool hostPool;
    private List<HostElement> hosts;

    public MinimumPenaltyCalculator(IVmPool vmPool, IHostPool hostPool, HostFilter hostFilter, IUserFairshareRecordManager userRecordManager, IVmFairshareRecordManager vmRecordManager) {
        super(vmPool, userRecordManager, vmRecordManager);
        this.hostFilter = hostFilter;
        this.hostPool = hostPool;       
        this.hosts = hostPool.getHosts(); 
    }    
    
    @Override  
    protected float getPenalty(VmElement vm) {
        List<HostElement> filteredHosts = hostFilter.getFilteredHosts(hosts, vm);
        HostElement firstHost = filteredHosts.get(0);
        float minPenalty = getHostPenalty(vm, firstHost);
        for (int i=1; i<filteredHosts.size(); i++) {
            float penalty = getHostPenalty(vm, filteredHosts.get(i));
            if (penalty < minPenalty) {
                minPenalty = penalty;
            }
        } 
        return minPenalty;  
    }
    
    private float getHostPenalty(VmElement vm, HostElement host) {
        return Math.max(vm.getCpu()/host.getMax_cpu(), ((float)vm.getMemory())/host.getMax_mem()) * host.getMax_cpu();
    }    
}
