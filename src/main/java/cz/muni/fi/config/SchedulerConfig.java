package cz.muni.fi.config;

import cz.muni.fi.scheduler.authorization.AuthorizationManager;
import cz.muni.fi.scheduler.authorization.AuthorizationManagerXml;
import cz.muni.fi.scheduler.authorization.IAuthorizationManager;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * The class is responsible for creating beans for the basic scheduler components.
 * 
 * @author Andras Urge
 */
@Configuration
@Import({PoolConfig.class, FilterConfig.class, FairshareConfig.class, SchedulingConfig.class, ResultConfig.class})
public class SchedulerConfig {
    
    private PropertiesConfig properties;    
    
    @Autowired PoolConfig poolConfig;
    @Autowired FilterConfig filterConfig;
    @Autowired FairshareConfig fairshareConfig;
    @Autowired SchedulingConfig schedulingConfig;
    @Autowired ResultConfig resultConfig;
    
    private static final String HOST_LOAD_AWARE_POLICY = "LoadAware";
    private static final String HOST_PACKING_POLICY = "Packing";  
    private static final String HOST_STRIPING_POLICY = "Striping";
    
    private static final String DS_PACKING_POLICY = "StoragePacking";  
    private static final String DS_STRIPING_POLICY = "StorageStriping";
    
    protected final Logger log = LoggerFactory.getLogger(getClass());

    public SchedulerConfig() throws IOException {
        properties = new PropertiesConfig("configuration.properties");
    }
    
    /**
     * Creates the scheduler instance.
     * @return the scheduler instance.
     * @throws LoadingFailedException 
     */
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
                             properties.getInt("numberofqueues"), 
                             properties.getBoolean("preferHostFit"),
                             schedulingConfig.queueMapper(),
                             schedulingConfig.vmSelector(),
                             schedulingConfig.limitChecker());
    }
    
    /**
     * Creates an instance of the placement policy given in configuration file.
     * @return the placement policy
     * @throws LoadingFailedException 
     */
    @Bean
    public IPlacementPolicy placementPolicy() throws LoadingFailedException {
        switch(properties.getString("hostPolicy")) {
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
    
    /**
     * Creates an instance of the storage policy given in configuration file.
     * @return the storage policy
     * @throws LoadingFailedException 
     */
    @Bean
    public IStoragePolicy storagePolicy() throws LoadingFailedException {
        switch(properties.getString("datastorePolicy")) {
            case DS_PACKING_POLICY:
                return new StoragePacking();
            case DS_STRIPING_POLICY:
                return new StorageStriping();
            default:
                throw new LoadingFailedException("Wrong storage policy configuration.");
        }
    }
    
    /**
     * Creates an instance of the authorization manager given.
     * The returned instance depends on the configuration.
     * 
     * @return the authorization manager.
     * @throws LoadingFailedException 
     */
    @Bean 
    public IAuthorizationManager authorizationManager() throws LoadingFailedException {
        if (properties.getBoolean("testingMode")) {
            return new AuthorizationManagerXml(poolConfig.hostPool(), poolConfig.datastorePool());
        } else {
            return new AuthorizationManager(poolConfig.aclPool(), poolConfig.clusterPool(), poolConfig.hostPool(), poolConfig.datastorePool(), poolConfig.userPool(), poolConfig.groupPool());
        }        
    }
}
