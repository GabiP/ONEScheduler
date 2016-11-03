/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.config;

import cz.muni.fi.scheduler.PropertiesConfig;
import cz.muni.fi.scheduler.fairshare.historyrecords.IUserFairshareRecordManager;
import cz.muni.fi.scheduler.fairshare.historyrecords.IVmFairshareRecordManager;
import cz.muni.fi.scheduler.fairshare.historyrecords.UserFairshareRecordManager;
import cz.muni.fi.scheduler.fairshare.historyrecords.VmFairshareRecordManager;
import java.io.IOException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author Andras Urge
 */
@Configuration
public class RecordManagerConfig {
    
    private PropertiesConfig properties;
    
    private static final String USER_RECORDS_PATH = "userRecords.txt";
    private static final String VM_RECORDS_PATH = "vmRecords.txt";
    private static final String USER_RECORDS_PATH_XML = "userRecordsXml.txt";
    private static final String VM_RECORDS_PATH_XML = "vmRecordsXml.txt";
    
    public RecordManagerConfig() throws IOException {
        properties = new PropertiesConfig("configuration.properties");
    }
    
    @Bean
    public IUserFairshareRecordManager userRecordManager() {
        if (properties.getBoolean("useXml")) {
            return new UserFairshareRecordManager(USER_RECORDS_PATH_XML);  
        } else {            
            return new UserFairshareRecordManager(USER_RECORDS_PATH); 
        }
    }
    
    @Bean 
    public IVmFairshareRecordManager vmRecordManager() {
        if (properties.getBoolean("useXml")) {
            return new VmFairshareRecordManager(VM_RECORDS_PATH_XML);  
        } else {            
            return new VmFairshareRecordManager(VM_RECORDS_PATH); 
        } 
    }
}
