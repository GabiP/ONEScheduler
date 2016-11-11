/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.config;

import cz.muni.fi.exceptions.LoadingFailedException;
import cz.muni.fi.one.pools.AclElementPool;
import cz.muni.fi.one.pools.ClusterElementPool;
import cz.muni.fi.one.pools.DatastoreElementPool;
import cz.muni.fi.one.pools.HostElementPool;
import cz.muni.fi.one.pools.UserElementPool;
import cz.muni.fi.one.pools.VmElementPool;
import cz.muni.fi.scheduler.setup.PropertiesConfig;
import cz.muni.fi.scheduler.elementpools.IAclPool;
import cz.muni.fi.scheduler.elementpools.IClusterPool;
import cz.muni.fi.scheduler.elementpools.IDatastorePool;
import cz.muni.fi.scheduler.elementpools.IHostPool;
import cz.muni.fi.scheduler.elementpools.IUserPool;
import cz.muni.fi.scheduler.elementpools.IVmPool;
import cz.muni.fi.xml.pools.ClusterXmlPool;
import cz.muni.fi.xml.pools.DatastoreXmlPool;
import cz.muni.fi.xml.pools.HostXmlPool;
import cz.muni.fi.xml.pools.UserXmlPool;
import cz.muni.fi.xml.pools.VmXmlPool;
import java.io.IOException;
import org.opennebula.client.Client;
import org.opennebula.client.ClientConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author Andras Urge
 */
@Configuration
public class PoolConfig {
    
    private PropertiesConfig properties;
    
    protected final Logger log = LoggerFactory.getLogger(getClass());
    
    public PoolConfig() throws IOException {
        properties = new PropertiesConfig("configuration.properties");
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
            log.error("ONe Client failed to instatiate!");
            throw new LoadingFailedException(ex.getMessage(), ex.getCause());
        }
    }   
}
