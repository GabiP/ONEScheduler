package cz.muni.fi.config;

import cz.muni.fi.exceptions.LoadingFailedException;
import cz.muni.fi.scheduler.setup.PropertiesConfig;
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
import cz.muni.fi.scheduler.filters.hosts.strategies.FilterHostBySchedulingRequirements;
import cz.muni.fi.scheduler.filters.hosts.strategies.IHostFilterStrategy;
import cz.muni.fi.scheduler.filters.hosts.strategies.ISchedulingHostFilterStrategy;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * The class is responsible for creating beans for filtering related classes.
 * 
 * @author Andras Urge
 */
@Configuration
@Import({PoolConfig.class})
public class FilterConfig {
    
    @Autowired PoolConfig poolConfig;
    
    private PropertiesConfig properties;
    
    private static final String HOST_CPU_FILTER = "FilterHostByCpu";
    private static final String HOST_MEMORY_FILTER = "FilterHostByMemory";  
    private static final String HOST_PCI_FILTER = "FilterHostByPci";
    private static final String HOST_SCHED_REQ_FILTER = "FilterHostBySchedulingRequirements";
    
    private static final String DS_STORAGE_FILTER = "FilterDatastoresByStorage";
    private static final String DS_SCHED_REQ_FILTER = "FilterDatastoresBySchedulingRequirements";
    
    public FilterConfig() throws IOException {
        properties = new PropertiesConfig("configuration.properties");
    }
    
    /**
     * Returns an instance of the configured HostFilter.
     * @return the host filter.
     * @throws LoadingFailedException 
     */
    @Bean
    public HostFilter fairshareHostFilter() throws LoadingFailedException {
        String[] filterConfig = properties.getStringArray("hostFilters");
        List<IHostFilterStrategy> filterStrategies = new ArrayList<>();
        filterStrategies.add(new FilterHostByMaxCpu());
        filterStrategies.add(new FilterHostByMaxMemory());
        for (String aFilterConfig : filterConfig) {
            switch (aFilterConfig) {
                case HOST_PCI_FILTER:
                    filterStrategies.add(new FilterHostByPci());
                    break;
                case HOST_SCHED_REQ_FILTER:
                    filterStrategies.add(new FilterHostBySchedulingRequirements());
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
    
    /**
     * Returns an instance of the configured SchedulingFilter.
     * @return the scheduling filter.
     * @throws LoadingFailedException 
     */
    @Bean
    public SchedulingHostFilter schedulingHostFilter() throws LoadingFailedException {
        String[] filterConfig = properties.getStringArray("hostFilters");
        List<IHostFilterStrategy> filterStrategies = new ArrayList<>();
        List<ISchedulingHostFilterStrategy> schedulingFilterStrategies = new ArrayList<>();
        for (String aFilterConfig : filterConfig) {
            switch (aFilterConfig) {
                case HOST_CPU_FILTER:
                    schedulingFilterStrategies.add(new FilterHostByCpu(poolConfig.clusterPool(), properties.getBoolean("testingMode")));
                    break;
                case HOST_MEMORY_FILTER:
                    schedulingFilterStrategies.add(new FilterHostByMemory(poolConfig.clusterPool(), properties.getBoolean("testingMode")));
                    break;
                case HOST_PCI_FILTER:
                    filterStrategies.add(new FilterHostByPci());
                    break;
                case HOST_SCHED_REQ_FILTER:
                    filterStrategies.add(new FilterHostBySchedulingRequirements());
                    break;
                default:
                    throw new LoadingFailedException("Wrong host filter configuration.");
            }
        }
        
        return new SchedulingHostFilter(filterStrategies, schedulingFilterStrategies);
    }
    
    /**
     * Returns an instance of the configured SchedulingDatastoreFilter.
     * @return the scheduling filter for datastore.
     * @throws LoadingFailedException 
     */
    @Bean
    public SchedulingDatastoreFilter schedulingDsFilter() throws LoadingFailedException {
        String[] filterConfig = properties.getStringArray("datastoreFilters");
        List<IDatastoreFilterStrategy> filterStrategies = new ArrayList<>();
        List<ISchedulingDatastoreFilterStrategy> schedulingFilterStrategies = new ArrayList<>();
        for (String aFilterConfig : filterConfig) {
            switch (aFilterConfig) {
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
}
