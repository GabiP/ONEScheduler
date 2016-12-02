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
import cz.muni.fi.scheduler.resources.HostElement;
import cz.muni.fi.scheduler.resources.VmElement;
import cz.muni.fi.scheduler.setup.PropertiesConfig;

/**
 * This class calculates the penalty of a Virtual Machine by comparing the
 * desired resources to the resources of every deployable host. The penalty
 * is calculated using the host that gives the smallest possible penalty.
 * 
 * @author Andras Urge
 */
public class RootBasedMpCalculator extends MinimumPenaltyCalculator { 

    public RootBasedMpCalculator(IHostPool hostPool, IDatastorePool dsPool, IClusterPool clusterPool, HostFilter hostFilter, PropertiesConfig fairshareConfig) {
        super(hostPool, dsPool, clusterPool, hostFilter, fairshareConfig);
    }        
    
    @Override
    protected float getHostPenalty(VmElement vm, HostElement host) {
        float cpuWeight = fairshareConfig.getFloat("cpuWeight");
        float ramWeight = fairshareConfig.getFloat("ramWeight");
        float hddWeight = fairshareConfig.getFloat("hddWeight");
        
        float cpuShare = vm.getCpu() / host.getMax_cpu();
        float ramShare = ((float)vm.getMemory()) / host.getMax_mem();
        float hddShare = ((float)vm.getDiskSizes()) / getHostStorageShare(host);
        
        float resourcePenalty = (float) (1 - Math.pow(1/(cpuWeight + ramWeight + hddWeight),
                Math.pow(1 - cpuShare, cpuWeight)*
                Math.pow(1 - ramShare, ramWeight)*
                Math.pow(1 - hddShare, hddWeight)));        
        return resourcePenalty * host.getMax_cpu();
    }  
}
