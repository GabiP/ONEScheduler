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
import cz.muni.fi.scheduler.elements.ClusterElement;
import cz.muni.fi.scheduler.elements.DatastoreElement;
import cz.muni.fi.scheduler.elements.HostElement;
import cz.muni.fi.scheduler.elements.VmElement;
import cz.muni.fi.scheduler.elements.nodes.DatastoreNode;
import cz.muni.fi.scheduler.setup.FairshareConfiguration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private IClusterPool clusterPool;    
    
    protected FairshareConfiguration fairshareConfig;
    
    protected List<HostElement> hosts;
    protected Map<Integer, Float> dsHostShare;

    public MinimumPenaltyCalculator(IHostPool hostPool, IDatastorePool dsPool, IClusterPool clusterPool, HostFilter hostFilter, FairshareConfiguration fairshareConfig) {
        this.hostFilter = hostFilter;
        this.hostPool = hostPool;     
        this.dsPool = dsPool;           
        this.clusterPool = clusterPool;  
        this.fairshareConfig = fairshareConfig;
        hosts = hostPool.getHosts();  
        dsHostShare = getDsHostShare();        
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
        
    private Map<Integer, Float> getDsHostShare() {
        Map<Integer, Float> result = new HashMap<>();
        for (DatastoreElement ds : dsPool.getSystemDs()) {
            if (ds.isShared() && ds.isMonitored()) {  
                int dsHosts = 0;
                for (int clusterId : ds.getClusters()) {
                    ClusterElement cluster = clusterPool.getCluster(clusterId);
                    dsHosts += cluster.getHosts().size();
                } 
                result.put(ds.getId(), (float)ds.getTotal_mb()/dsHosts);
            }
        }
        return result;
    }
    
    protected float getHostStorageShare(HostElement host) {
        float hostLocalStorage = 0;
        for (DatastoreNode ds : host.getDatastores()) {
            hostLocalStorage += ds.getTotal_mb();
        }
        float hostSharedDsStorage = 0;
        ClusterElement cluster = clusterPool.getCluster(host.getClusterId());
        for (int ds : cluster.getDatastores()) {
            if (dsHostShare.containsKey(ds)) {
                hostSharedDsStorage += dsHostShare.get(ds);
            }
        }
        return hostLocalStorage + hostSharedDsStorage;
    }
    
    protected abstract float getHostPenalty(VmElement vm, HostElement host);
}
