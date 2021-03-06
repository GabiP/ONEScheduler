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
 * is calculated by a Max-based function using the host that gives the smallest 
 * possible penalty.
 * 
 * @author Andras Urge
 */
public class MaxBasedMpCalculator extends MinimumPenaltyCalculator { 

    public MaxBasedMpCalculator(IHostPool hostPool, IDatastorePool dsPool, IClusterPool clusterPool, HostFilter hostFilter, FairshareConfiguration fairshareConfig) {
        super(hostPool, dsPool, clusterPool, hostFilter, fairshareConfig);
    }        
    
    /**
     * Penalty calculated for the pair of a virtual machine and a host using a 
     * Max-based function.
     * 
     * @param vm the virtual machine
     * @param host the host
     * @return the penalty of the vm for this host
     */
    @Override
    protected float getHostPenalty(VmElement vm, HostElement host) {
        
        float maxResource = Math.max(Math.max(
                                (vm.getCpu()/host.getMax_cpu()) * fairshareConfig.getCpuWeight(), 
                                ((float)vm.getMemory()/host.getMax_mem()) * fairshareConfig.getRamWeight()),
                                ((float)vm.getDiskSizes()/getHostStorageShare(host)) * fairshareConfig.getHddWeight());
        return maxResource * host.getMax_cpu();
    }  
}
