package cz.muni.fi.config;

import cz.muni.fi.exceptions.LoadingFailedException;
import cz.muni.fi.scheduler.fairshare.UserPriorityCalculator;
import cz.muni.fi.scheduler.fairshare.penaltycalculators.IVmPenaltyCalculator;
import cz.muni.fi.scheduler.fairshare.penaltycalculators.CpuTimeCalculator;
import cz.muni.fi.scheduler.fairshare.penaltycalculators.MaxBasedMpCalculator;
import cz.muni.fi.scheduler.fairshare.penaltycalculators.ProcessorEquivalentCalculator;
import cz.muni.fi.scheduler.fairshare.penaltycalculators.RootBasedMpCalculator;
import cz.muni.fi.scheduler.setup.FairshareConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 *
 * @author Andras Urge
 */
@Configuration
@Import({PoolConfig.class, FilterConfig.class, RecordManagerConfig.class})
public class FairshareConfig {
    
    private static final String CONFIG_PATH = "fairshare.properties";
    
    @Autowired PoolConfig poolConfig;
    @Autowired FilterConfig filterConfig;
    @Autowired RecordManagerConfig recConfig;    
        
    private static final String CPU_TIME = "Cpu";
    private static final String PROC_EQ = "PE";
    private static final String MAX_MP = "MaxBasedMP";
    private static final String ROOT_MP = "RootBasedMP";
        
    @Bean 
    public UserPriorityCalculator userPriorityCalculator() throws LoadingFailedException {
        return new UserPriorityCalculator(poolConfig.vmPool(), vmPenaltyCalculator(), recConfig.userRecordManager(), recConfig.vmRecordManager());     
    }
    
    @Bean 
    public IVmPenaltyCalculator vmPenaltyCalculator() throws LoadingFailedException {        
        FairshareConfiguration config = new FairshareConfiguration(CONFIG_PATH);        
        switch (config.getPenaltyFunction()) {
            case CPU_TIME:
                return new CpuTimeCalculator();
            case PROC_EQ:
                return new ProcessorEquivalentCalculator(poolConfig.hostPool(), poolConfig.datastorePool(), config);
            case MAX_MP:  
                return new MaxBasedMpCalculator(
                        poolConfig.hostPool(), 
                        poolConfig.datastorePool(), 
                        poolConfig.clusterPool(), 
                        filterConfig.fairshareHostFilter(), 
                        config);
            case ROOT_MP:  
                return new RootBasedMpCalculator(
                        poolConfig.hostPool(), 
                        poolConfig.datastorePool(), 
                        poolConfig.clusterPool(), 
                        filterConfig.fairshareHostFilter(), 
                        config);
            default:    
                throw new LoadingFailedException("Incorrect fairshare configuration - wrong penalty function.");
        } 
    }
}
