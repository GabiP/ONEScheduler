/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.scheduler.fairshare.calculators;

import cz.muni.fi.scheduler.elementpools.IDatastorePool;
import cz.muni.fi.scheduler.elementpools.IHostPool;
import cz.muni.fi.scheduler.filters.hosts.HostFilter;
import cz.muni.fi.scheduler.resources.DatastoreElement;
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
public abstract class MinimumPenaltyCalculator implements IVmPenaltyCalculator {
    
    private HostFilter hostFilter;
    private IHostPool hostPool;
    private IDatastorePool dsPool;
    
    protected List<HostElement> hosts;
    protected float sharedDsStorage;

    public MinimumPenaltyCalculator(IHostPool hostPool, IDatastorePool dsPool, HostFilter hostFilter) {
        this.hostFilter = hostFilter;
        this.hostPool = hostPool;     
        this.dsPool = dsPool;           
        hosts = hostPool.getHosts();  
        sharedDsStorage = getSharedDsStorage();
    }    
    
    @Override  
    public float getPenalty(VmElement vm) {
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
    
    private float getSharedDsStorage() {
        int storage = 0;
        for (DatastoreElement ds : dsPool.getSystemDs()) {
            if (ds.isShared() && ds.isMonitored()) {                
                storage += ds.getTotal_mb();
            }
        }
        return storage;
    }    
    
    protected abstract float getHostPenalty(VmElement vm, HostElement host);
}
