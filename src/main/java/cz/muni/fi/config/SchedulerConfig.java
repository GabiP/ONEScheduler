/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.config;

import cz.muni.fi.authorization.AuthorizationManager;
import cz.muni.fi.authorization.AuthorizationManagerXml;
import cz.muni.fi.authorization.IAuthorizationManager;
import cz.muni.fi.exceptions.LoadingFailedException;
import cz.muni.fi.scheduler.setup.PropertiesConfig;
import cz.muni.fi.scheduler.core.Scheduler;
import cz.muni.fi.scheduler.policies.datastores.IStoragePolicy;
import cz.muni.fi.scheduler.policies.datastores.StoragePacking;
import cz.muni.fi.scheduler.policies.datastores.StorageStriping;
import cz.muni.fi.scheduler.policies.hosts.IPlacementPolicy;
import cz.muni.fi.scheduler.policies.hosts.LoadAware;
import cz.muni.fi.scheduler.policies.hosts.Packing;
import cz.muni.fi.scheduler.policies.hosts.Striping;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 *
 * @author Andras Urge
 */
@Configuration
@Import({PoolConfig.class, FilterConfig.class, FairshareConfig.class})
public class SchedulerConfig {
    
    private PropertiesConfig properties;    
    
    @Autowired PoolConfig poolConfig;
    @Autowired FilterConfig filterConfig;
    @Autowired FairshareConfig fairshareConfig; 
    
    private static final String HOST_LOAD_AWARE_POLICY = "LoadAware";
    private static final String HOST_PACKING_POLICY = "Packing";  
    private static final String HOST_STRIPING_POLICY = "Striping";
    
    private static final String DS_PACKING_POLICY = "StoragePacking";  
    private static final String DS_STRIPING_POLICY = "StorageStriping";

    public SchedulerConfig() throws IOException {
        properties = new PropertiesConfig("configuration.properties");
    }
    
    @Bean 
    public Scheduler scheduler() throws LoadingFailedException {
        return new Scheduler(authorizationManager(), 
                             poolConfig.hostPool(),
                             poolConfig.vmPool(), 
                             poolConfig.datastorePool(), 
                             filterConfig.schedulingHostFilter(), 
                             filterConfig.schedulingDsFilter(), 
                             placementPolicy(), 
                             storagePolicy(), 
                             fairshareConfig.fairshareOrderer(), 
                             properties.getInt("numberofqueues"), 
                             properties.getBoolean("preferHostFit"));
    }
    
    @Bean
    public IPlacementPolicy placementPolicy() throws LoadingFailedException {
        switch(properties.getString("hostPolicies")) {
            case HOST_LOAD_AWARE_POLICY:
                return new LoadAware();
            case HOST_PACKING_POLICY:
                return new Packing();
            case HOST_STRIPING_POLICY:
                return new Striping();
            default:
                throw new LoadingFailedException("Wrong placement policy configuration.");
        }
    }
    
    @Bean
    public IStoragePolicy storagePolicy() throws LoadingFailedException {
        switch(properties.getString("datastorePolicies")) {
            case DS_PACKING_POLICY:
                return new StoragePacking();
            case DS_STRIPING_POLICY:
                return new StorageStriping();
            default:
                throw new LoadingFailedException("Wrong storage policy configuration.");
        }
    }
    
    @Bean 
    public IAuthorizationManager authorizationManager() throws LoadingFailedException {
        if (properties.getBoolean("useXml")) {
            return new AuthorizationManagerXml(poolConfig.hostPool());
        } else {
            return new AuthorizationManager(poolConfig.aclPool(), poolConfig.clusterPool(), poolConfig.hostPool(), poolConfig.datastorePool(), poolConfig.userPool());
        }        
    }
}
