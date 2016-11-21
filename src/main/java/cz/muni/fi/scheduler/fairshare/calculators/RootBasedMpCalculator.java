/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.scheduler.fairshare.calculators;

import cz.muni.fi.scheduler.elementpools.IDatastorePool;
import cz.muni.fi.scheduler.elementpools.IHostPool;
import cz.muni.fi.scheduler.filters.hosts.HostFilter;
import cz.muni.fi.scheduler.resources.HostElement;
import cz.muni.fi.scheduler.resources.VmElement;

/**
 * This class calculates the penalty of a Virtual Machine by comparing the
 * desired resources to the resources of every deployable host. The penalty
 * is calculated using the host that gives the smallest possible penalty.
 * 
 * @author Andras Urge
 */
public class RootBasedMpCalculator extends MinimumPenaltyCalculator { 

    public RootBasedMpCalculator(IHostPool hostPool, IDatastorePool dsPool, HostFilter hostFilter) {
        super(hostPool, dsPool, hostFilter);
    }        
    
    @Override
    protected float getHostPenalty(VmElement vm, HostElement host) {
        // TODO: host.getMax_disk() may not be really the size of the datastores belonging to this host
        float hostDsStorage = host.getMax_disk() + sharedDsStorage/hosts.size();
        float resourcePenalty = (float) (1 - Math.cbrt(
                (1 - vm.getCpu()/host.getMax_cpu()) *
                        (1 - ((float)vm.getMemory())/host.getMax_mem()) *
                        (1 - ((float)vm.getDiskSizes())/hostDsStorage)));        
        return resourcePenalty * host.getMax_cpu();
    }  
}
