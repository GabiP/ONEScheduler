/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.config;

import cz.muni.fi.exceptions.LoadingFailedException;
import cz.muni.fi.scheduler.setup.PropertiesConfig;
import cz.muni.fi.scheduler.fairshare.AbstractPriorityCalculator;
import cz.muni.fi.scheduler.fairshare.FairShareOrderer;
import cz.muni.fi.scheduler.fairshare.calculators.MaxMinCalculator;
import cz.muni.fi.scheduler.fairshare.calculators.MinimumPenaltyCalculator;
import cz.muni.fi.scheduler.fairshare.calculators.ProcessorEquivalentCalculator;
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
    
    private static final String MAX_MIN = "MaxMin";
    private static final String PROC_EQ = "ProcessorEquivalent";
    private static final String MIN_PENALTY = "MinimumPenalty";
    
    public FairshareConfig() throws IOException {
        properties = new PropertiesConfig("configuration.properties");
    }
    
    @Bean 
    public FairShareOrderer fairshareOrderer() throws LoadingFailedException {
        return new FairShareOrderer(priorityCalculator());     
    }
    
    @Bean 
    public AbstractPriorityCalculator priorityCalculator() throws LoadingFailedException {
        switch (properties.getString("fairshare")) {
            case MAX_MIN:
                return new MaxMinCalculator(poolConfig.vmPool(), recConfig.userRecordManager(), recConfig.vmRecordManager());
            case PROC_EQ:
                return new ProcessorEquivalentCalculator(poolConfig.vmPool(), poolConfig.hostPool(), recConfig.userRecordManager(), recConfig.vmRecordManager());
            case MIN_PENALTY:  
                return new MinimumPenaltyCalculator(poolConfig.vmPool(), poolConfig.hostPool(), filterConfig.fairshareHostFilter(), recConfig.userRecordManager(), recConfig.vmRecordManager());
            default:    
                throw new LoadingFailedException("Wrong fairshare configuration.");
        }   
    }
}
