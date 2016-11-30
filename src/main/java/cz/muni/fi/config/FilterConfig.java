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
import cz.muni.fi.scheduler.filters.hosts.strategies.FilterHostsBySchedulingRequirements;
import cz.muni.fi.scheduler.filters.hosts.strategies.IHostFilterStrategy;
import cz.muni.fi.scheduler.filters.hosts.strategies.ISchedulingHostFilterStrategy;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author Andras Urge
 */
@Configuration
public class FilterConfig {
    
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
}
