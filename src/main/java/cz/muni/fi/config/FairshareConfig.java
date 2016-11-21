/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.config;

import cz.muni.fi.exceptions.LoadingFailedException;
import cz.muni.fi.scheduler.setup.PropertiesConfig;
import cz.muni.fi.scheduler.fairshare.UserPriorityCalculator;
import cz.muni.fi.scheduler.fairshare.UserFairShareOrderer;
import cz.muni.fi.scheduler.fairshare.IFairShareOrderer;
import cz.muni.fi.scheduler.fairshare.calculators.IVmPenaltyCalculator;
import cz.muni.fi.scheduler.fairshare.calculators.CpuTimeCalculator;
import cz.muni.fi.scheduler.fairshare.calculators.MaxBasedMpCalculator;
import cz.muni.fi.scheduler.fairshare.calculators.MinimumPenaltyCalculator;
import cz.muni.fi.scheduler.fairshare.calculators.ProcessorEquivalentCalculator;
import cz.muni.fi.scheduler.fairshare.calculators.RootBasedMpCalculator;
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
@Import({PoolConfig.class, FilterConfig.class, RecordManagerConfig.class})
public class FairshareConfig {
    
    private PropertiesConfig properties;
    
    @Autowired PoolConfig poolConfig;
    @Autowired FilterConfig filterConfig;
    @Autowired RecordManagerConfig recConfig;    
    
    private static final String CPU_TIME = "CpuTime";
    private static final String PROC_EQ = "PE";
    private static final String MAX_MP = "MaxBasedMP";
    private static final String ROOT_MP = "RootBasedMP";
    
    public FairshareConfig() throws IOException {
        properties = new PropertiesConfig("configuration.properties");
    }
    
    @Bean 
    public IFairShareOrderer fairshareOrderer() throws LoadingFailedException {
        return new UserFairShareOrderer(userPriorityCalculator());     
    }
    
    @Bean 
    public UserPriorityCalculator userPriorityCalculator() throws LoadingFailedException {
        return new UserPriorityCalculator(poolConfig.vmPool(), vmPenaltyCalculator(), recConfig.userRecordManager(), recConfig.vmRecordManager());     
    }
    
    @Bean 
    public IVmPenaltyCalculator vmPenaltyCalculator() throws LoadingFailedException {
        switch (properties.getString("fairshare")) {
            case CPU_TIME:
                return new CpuTimeCalculator();
            case PROC_EQ:
                return new ProcessorEquivalentCalculator(poolConfig.hostPool(), poolConfig.datastorePool());
            case MAX_MP:  
                return new MaxBasedMpCalculator(poolConfig.hostPool(), poolConfig.datastorePool(), filterConfig.fairshareHostFilter());
            case ROOT_MP:  
                return new RootBasedMpCalculator(poolConfig.hostPool(), poolConfig.datastorePool(), filterConfig.fairshareHostFilter());
            default:    
                throw new LoadingFailedException("Wrong fairshare configuration.");
        }   
    }
}
