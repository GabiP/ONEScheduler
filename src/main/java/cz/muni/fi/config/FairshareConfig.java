/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.config;

import cz.muni.fi.exceptions.LoadingFailedException;
import cz.muni.fi.scheduler.setup.PropertiesConfig;
import cz.muni.fi.scheduler.fairshare.UserPriorityCalculator;
import cz.muni.fi.scheduler.fairshare.penaltycalculators.IVmPenaltyCalculator;
import cz.muni.fi.scheduler.fairshare.penaltycalculators.CpuTimeCalculator;
import cz.muni.fi.scheduler.fairshare.penaltycalculators.MaxBasedMpCalculator;
import cz.muni.fi.scheduler.fairshare.penaltycalculators.ProcessorEquivalentCalculator;
import cz.muni.fi.scheduler.fairshare.penaltycalculators.RootBasedMpCalculator;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
    
    @Autowired PoolConfig poolConfig;
    @Autowired FilterConfig filterConfig;
    @Autowired RecordManagerConfig recConfig;    
    
    private static final String CONFIG_PATH = "config" + File.separator + "fairshare.properties";
    
    private static final String CPU_TIME = "CpuTime";
    private static final String PROC_EQ = "PE";
    private static final String MAX_MP = "MaxBasedMP";
    private static final String ROOT_MP = "RootBasedMP";
    
    private static final String CPU_WEIGHT = "cpuWeight";
    private static final String RAM_WEIGHT = "ramWeight";
    private static final String HDD_WEIGHT = "hddWeight";    
    
        
    @Bean 
    public UserPriorityCalculator userPriorityCalculator() throws LoadingFailedException {
        return new UserPriorityCalculator(poolConfig.vmPool(), vmPenaltyCalculator(), recConfig.userRecordManager(), recConfig.vmRecordManager());     
    }
    
    @Bean 
    public IVmPenaltyCalculator vmPenaltyCalculator() throws LoadingFailedException {
        switch (fairshareConfig().getString("penaltyFunction")) {
            case CPU_TIME:
                return new CpuTimeCalculator();
            case PROC_EQ:
                return new ProcessorEquivalentCalculator(poolConfig.hostPool(), poolConfig.datastorePool(), fairshareConfig());
            case MAX_MP:  
                return new MaxBasedMpCalculator(
                        poolConfig.hostPool(), 
                        poolConfig.datastorePool(), 
                        poolConfig.clusterPool(), 
                        filterConfig.fairshareHostFilter(), 
                        fairshareConfig());
            case ROOT_MP:  
                return new RootBasedMpCalculator(
                        poolConfig.hostPool(), 
                        poolConfig.datastorePool(), 
                        poolConfig.clusterPool(), 
                        filterConfig.fairshareHostFilter(), 
                        fairshareConfig());
            default:    
                throw new LoadingFailedException("Incorrect fairshare configuration in " + CONFIG_PATH + " - wrong penalty function.");
        }   
    }
    
    @Bean 
    public PropertiesConfig fairshareConfig() throws LoadingFailedException {
        try {
            PropertiesConfig properties = new PropertiesConfig(CONFIG_PATH);
            List<Float> resourceWeights = new ArrayList<>();
            resourceWeights.add(properties.getFloat(CPU_WEIGHT));
            resourceWeights.add(properties.getFloat(RAM_WEIGHT));
            resourceWeights.add(properties.getFloat(HDD_WEIGHT));
            
            float weightSum = 0;
            for (float weight : resourceWeights) {
                if (weight < 0) {
                    throw new LoadingFailedException("Incorrect fairshare configuration in " + CONFIG_PATH + " - resource weights cannot be smaller than 0.");
                }
                weightSum += weight;
            }
            if (weightSum == 0) {
                throw new LoadingFailedException("Incorrect fairshare configuration in " + CONFIG_PATH + " - all resource weights cannot be 0.");
            }           
            
            return properties;
        } catch (IOException ex) {
            throw new LoadingFailedException(ex.toString());
        }
    }

    public static String getConfigPath() {
        return CONFIG_PATH;
    }
}
