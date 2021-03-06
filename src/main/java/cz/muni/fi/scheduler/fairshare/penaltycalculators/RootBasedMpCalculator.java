/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.scheduler.fairshare.penaltycalculators;

import cz.muni.fi.scheduler.elementpools.IClusterPool;
import cz.muni.fi.scheduler.elementpools.IDatastorePool;
import cz.muni.fi.scheduler.elementpools.IHostPool;
import cz.muni.fi.scheduler.filters.hosts.HostFilter;
import cz.muni.fi.scheduler.elements.HostElement;
import cz.muni.fi.scheduler.elements.VmElement;
import cz.muni.fi.scheduler.setup.FairshareConfiguration;

/**
 * This class calculates the penalty of a Virtual Machine by comparing the
 * desired resources to the resources of every deployable host. The penalty
 * is calculated by a Root-based function using the host that gives the smallest 
 * possible penalty.
 * 
 * @author Andras Urge
 */
public class RootBasedMpCalculator extends MinimumPenaltyCalculator { 

    public RootBasedMpCalculator(IHostPool hostPool, IDatastorePool dsPool, IClusterPool clusterPool, HostFilter hostFilter, FairshareConfiguration fairshareConfig) {
        super(hostPool, dsPool, clusterPool, hostFilter, fairshareConfig);
    }        
    
    /**
     * Penalty calculated for the pair of a virtual machine and a host using a 
     * Root-based function.
     * 
     * @param vm the virtual machine
     * @param host the host
     * @return the penalty of the vm for this host
     */
    @Override
    protected float getHostPenalty(VmElement vm, HostElement host) {
        float cpuWeight = fairshareConfig.getCpuWeight();
        float ramWeight = fairshareConfig.getRamWeight();
        float hddWeight = fairshareConfig.getHddWeight();
        
        float cpuShare = vm.getCpu() / host.getMax_cpu();
        float ramShare = ((float)vm.getMemory()) / host.getMax_mem();
        float hddShare = ((float)vm.getDiskSizes()) / getHostStorageShare(host);
        
        float resourcePenalty = (float) (1 - Math.pow(
                Math.pow(1 - cpuShare, cpuWeight)*
                Math.pow(1 - ramShare, ramWeight)*
                Math.pow(1 - hddShare, hddWeight), 
                1/(cpuWeight + ramWeight + hddWeight)));        
        return resourcePenalty * host.getMax_cpu();
    }  
}
