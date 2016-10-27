/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.scheduler;

import cz.muni.fi.authorization.AuthorizationManager;
import cz.muni.fi.authorization.AuthorizationManagerXml;
import cz.muni.fi.authorization.IAuthorizationManager;
import cz.muni.fi.exceptions.LoadingFailedException;
import cz.muni.fi.one.pools.AclElementPool;
import cz.muni.fi.one.pools.ClusterElementPool;
import cz.muni.fi.one.pools.DatastoreElementPool;
import cz.muni.fi.one.pools.HostElementPool;
import cz.muni.fi.one.pools.UserElementPool;
import cz.muni.fi.one.pools.VmElementPool;
import cz.muni.fi.scheduler.elementpools.IAclPool;
import cz.muni.fi.scheduler.elementpools.IClusterPool;
import cz.muni.fi.scheduler.elementpools.IDatastorePool;
import cz.muni.fi.scheduler.elementpools.IHostPool;
import cz.muni.fi.scheduler.elementpools.IUserPool;
import cz.muni.fi.scheduler.elementpools.IVmPool;
import cz.muni.fi.scheduler.fairshare.AbstractPriorityCalculator;
import cz.muni.fi.scheduler.fairshare.FairShareOrderer;
import cz.muni.fi.scheduler.fairshare.calculators.MaxMinCalculator;
import cz.muni.fi.scheduler.fairshare.calculators.MinimumPenaltyCalculator;
import cz.muni.fi.scheduler.fairshare.calculators.ProcessorEquivalentCalculator;
import cz.muni.fi.scheduler.fairshare.historyrecords.IUserFairshareRecordManager;
import cz.muni.fi.scheduler.fairshare.historyrecords.IVmFairshareRecordManager;
import cz.muni.fi.scheduler.fairshare.historyrecords.UserFairshareRecordManager;
import cz.muni.fi.scheduler.fairshare.historyrecords.VmFairshareRecordManager;
import cz.muni.fi.scheduler.filters.datastores.SchedulingDatastoreFilter;
import cz.muni.fi.scheduler.filters.datastores.strategies.FilterDatastoresBySchedulingRequirements;
import cz.muni.fi.scheduler.filters.datastores.strategies.FilterDatastoresByStorage;
import cz.muni.fi.scheduler.filters.datastores.strategies.IDatastoreFilterStrategy;
import cz.muni.fi.scheduler.filters.datastores.strategies.ISchedulingDatastoreFilterStrategy;
import cz.muni.fi.scheduler.filters.hosts.HostFilter;
import cz.muni.fi.scheduler.filters.hosts.SchedulingHostFilter;
import cz.muni.fi.scheduler.filters.hosts.strategies.FilterHostByCpu;
import cz.muni.fi.scheduler.filters.hosts.strategies.FilterHostByMaxCpu;
import cz.muni.fi.scheduler.filters.hosts.strategies.FilterHostByMaxMemory;
import cz.muni.fi.scheduler.filters.hosts.strategies.FilterHostByMemory;
import cz.muni.fi.scheduler.filters.hosts.strategies.FilterHostByPci;
import cz.muni.fi.scheduler.filters.hosts.strategies.FilterHostsBySchedulingRequirements;
import cz.muni.fi.scheduler.filters.hosts.strategies.IHostFilterStrategy;
import cz.muni.fi.scheduler.filters.hosts.strategies.ISchedulingHostFilterStrategy;
import cz.muni.fi.scheduler.policies.datastores.IStoragePolicy;
import cz.muni.fi.scheduler.policies.datastores.StoragePacking;
import cz.muni.fi.scheduler.policies.datastores.StorageStriping;
import cz.muni.fi.scheduler.policies.hosts.IPlacementPolicy;
import cz.muni.fi.scheduler.policies.hosts.LoadAware;
import cz.muni.fi.scheduler.policies.hosts.Packing;
import cz.muni.fi.scheduler.policies.hosts.Striping;
import cz.muni.fi.xml.pools.ClusterXmlPool;
import cz.muni.fi.xml.pools.DatastoreXmlPool;
import cz.muni.fi.xml.pools.HostXmlPool;
import cz.muni.fi.xml.pools.UserXmlPool;
import cz.muni.fi.xml.pools.VmXmlPool;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.opennebula.client.Client;
import org.opennebula.client.ClientConfigurationException;

/**
 *
 * @author Andras Urge
 */
@Configuration
public class BeanConfig {
    
    private PropertiesConfig properties;
    
    private static final String MAX_MIN = "MaxMin";
    private static final String PROC_EQ = "ProcessorEquivalent";
    private static final String MIN_PENALTY = "MinimumPenalty";
    
    private static final String HOST_CPU_FILTER = "FilterHostByCpu";
    private static final String HOST_MEMORY_FILTER = "FilterHostByMemory";  
    private static final String HOST_PCI_FILTER = "FilterHostByPci";
    private static final String HOST_SCHED_REQ_FILTER = "FilterHostBySchedulingRequirements";
    
    private static final String DS_STORAGE_FILTER = "FilterDatastoresByStorage";
    private static final String DS_SCHED_REQ_FILTER = "FilterDatastoresBySchedulingRequirements";
    
    private static final String HOST_LOAD_AWARE_POLICY = "LoadAware";
    private static final String HOST_PACKING_POLICY = "Packing";  
    private static final String HOST_STRIPING_POLICY = "Striping";
    
    private static final String DS_PACKING_POLICY = "StoragePacking";  
    private static final String DS_STRIPING_POLICY = "StorageStriping";

    public BeanConfig() throws IOException {
        properties = new PropertiesConfig("configuration.properties");
    }
    
    @Bean 
    public Scheduler scheduler() throws LoadingFailedException {
        return new Scheduler(authorizationManager(), 
                             hostPool(),
                             vmPool(), 
                             datastorePool(), 
                             schedulingHostFilter(), 
                             schedulingDsFilter(), 
                             placementPolicy(), 
                             storagePolicy(), 
                             fairshareOrderer(), 
                             properties.getInt("numberofqueues"), 
                             properties.getBoolean("preferHostFit"));
    }
    
    @Bean 
    public FairShareOrderer fairshareOrderer() throws LoadingFailedException {
        return new FairShareOrderer(priorityCalculator());     
    }
    
    @Bean 
    public AbstractPriorityCalculator priorityCalculator() throws LoadingFailedException {
        switch (properties.getString("fairshare")) {
            case MAX_MIN:
                return new MaxMinCalculator(vmPool(), userRecordManager(), vmRecordManager());
            case PROC_EQ:
                return new ProcessorEquivalentCalculator(vmPool(), hostPool(), userRecordManager(), vmRecordManager());
            case MIN_PENALTY:  
                return new MinimumPenaltyCalculator(vmPool(), hostPool(), fairshareHostFilter(), userRecordManager(), vmRecordManager());
            default:    
                throw new LoadingFailedException("Wrong fairshare configuration.");
        }   
    }
    
    @Bean 
    public IUserFairshareRecordManager userRecordManager() {
        return new UserFairshareRecordManager(properties.getString("userRecordsPath"));     
    }
    
    @Bean 
    public IVmFairshareRecordManager vmRecordManager() {
        return new VmFairshareRecordManager(properties.getString("vmRecordsPath"));     
    }
    
    @Bean
    public HostFilter fairshareHostFilter() throws LoadingFailedException {
        String[] filterConfig = properties.getStringArray("hostFilters");
        List<IHostFilterStrategy> filterStrategies = new ArrayList<>();
        filterStrategies.add(new FilterHostByMaxCpu());
        filterStrategies.add(new FilterHostByMaxMemory());
        for (int i=0; i<filterConfig.length; i++) {
            switch(filterConfig[i]) {
                case HOST_PCI_FILTER:
                    filterStrategies.add(new FilterHostByPci());
                    break;
                case HOST_SCHED_REQ_FILTER:
                    filterStrategies.add(new FilterHostsBySchedulingRequirements());
                    break;
                case HOST_CPU_FILTER:
                case HOST_MEMORY_FILTER:
                    break;
                default:
                    throw new LoadingFailedException("Wrong host filter configuration.");
            }
        }
        
        return new HostFilter(filterStrategies);
    }
    
    @Bean
    public SchedulingHostFilter schedulingHostFilter() throws LoadingFailedException {
        String[] filterConfig = properties.getStringArray("hostFilters");
        List<IHostFilterStrategy> filterStrategies = new ArrayList<>();
        List<ISchedulingHostFilterStrategy> schedulingFilterStrategies = new ArrayList<>();
        for (int i=0; i<filterConfig.length; i++) {
            switch(filterConfig[i]) {
                case HOST_CPU_FILTER:
                    schedulingFilterStrategies.add(new FilterHostByCpu());
                    break;
                case HOST_MEMORY_FILTER:
                    schedulingFilterStrategies.add(new FilterHostByMemory());
                    break;
                case HOST_PCI_FILTER:
                    filterStrategies.add(new FilterHostByPci());
                    break;
                case HOST_SCHED_REQ_FILTER:
                    filterStrategies.add(new FilterHostsBySchedulingRequirements());
                    break;
                default:
                    throw new LoadingFailedException("Wrong host filter configuration.");
            }
        }
        
        return new SchedulingHostFilter(filterStrategies, schedulingFilterStrategies);
    }
    
    @Bean
    public SchedulingDatastoreFilter schedulingDsFilter() throws LoadingFailedException {
        String[] filterConfig = properties.getStringArray("datastoreFilters");
        List<IDatastoreFilterStrategy> filterStrategies = new ArrayList<>();
        List<ISchedulingDatastoreFilterStrategy> schedulingFilterStrategies = new ArrayList<>();
        for (int i=0; i<filterConfig.length; i++) {
            switch(filterConfig[i]) {
                case DS_STORAGE_FILTER:
                    schedulingFilterStrategies.add(new FilterDatastoresByStorage());
                    break;
                case DS_SCHED_REQ_FILTER:
                    filterStrategies.add(new FilterDatastoresBySchedulingRequirements());
                    break;
                default:
                    throw new LoadingFailedException("Wrong datastore filter configuration.");
            }
        }
        
        return new SchedulingDatastoreFilter(filterStrategies, schedulingFilterStrategies);
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
            return new AuthorizationManagerXml(hostPool());
        } else {
            return new AuthorizationManager(aclPool(), clusterPool(), hostPool(), datastorePool(), userPool());
        }        
    }
    
    @Bean 
    public IAclPool aclPool() throws LoadingFailedException { 
        if (properties.getBoolean("useXml")) {
            return null;
        }
        return new AclElementPool(client());               
    }
    
    @Bean 
    public IVmPool vmPool() throws LoadingFailedException{
        if (properties.getBoolean("useXml")) {
            try {
                return new VmXmlPool(properties.getString("vmpoolpath"));
            } catch (IOException ex) {
                throw new LoadingFailedException(ex.getMessage(), ex.getCause());
            }
        } else {
            return new VmElementPool(client());
        }        
    }
    
    @Bean 
    public IUserPool userPool() throws LoadingFailedException{
        if (properties.getBoolean("useXml")) {
            try {
                return new UserXmlPool(properties.getString("userpoolpath"));
            } catch (IOException ex) {
                throw new LoadingFailedException(ex.getMessage(), ex.getCause());
            }
        } else {
            return new UserElementPool(client());
        }        
    }
    
    @Bean 
    public IHostPool hostPool() throws LoadingFailedException{
        if (properties.getBoolean("useXml")) {
            try {
                return new HostXmlPool(properties.getString("hostpoolpath"));
            } catch (IOException ex) {
                throw new LoadingFailedException(ex.getMessage(), ex.getCause());
            }
        } else {
            return new HostElementPool(client());
        }        
    }
    
    @Bean 
    public IClusterPool clusterPool() throws LoadingFailedException{
        if (properties.getBoolean("useXml")) {
            try {
                return new ClusterXmlPool(properties.getString("clusterpoolpath"));
            } catch (IOException ex) {
                throw new LoadingFailedException(ex.getMessage(), ex.getCause());
            }
        } else {
            return new ClusterElementPool(client());
        }        
    }
    
    @Bean 
    public IDatastorePool datastorePool() throws LoadingFailedException{
        if (properties.getBoolean("useXml")) {
            try {
                return new DatastoreXmlPool(properties.getString("datastorepoolpath"));
            } catch (IOException ex) {
                throw new LoadingFailedException(ex.getMessage(), ex.getCause());
            }
        } else {
            return new DatastoreElementPool(client());
        }        
    }
    
    @Bean 
    public Client client() throws LoadingFailedException{
        if (properties.getBoolean("useXml")) {
            return null;
        }
        try {
            return new Client(properties.getString("secret"), properties.getString("endpoint"));
        } catch (ClientConfigurationException ex) {
            throw new LoadingFailedException(ex.getMessage(), ex.getCause());
        }
    }    
}
