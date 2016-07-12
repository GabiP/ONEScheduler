/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.scheduler;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import cz.muni.fi.authorization.AuthorizationManagerXml;
import cz.muni.fi.authorization.IAuthorizationManager;
import cz.muni.fi.scheduler.elementpools.IClusterPool;
import cz.muni.fi.scheduler.elementpools.IDatastorePool;
import cz.muni.fi.scheduler.elementpools.IHostPool;
import cz.muni.fi.scheduler.elementpools.IUserPool;
import cz.muni.fi.scheduler.elementpools.IVmPool;
import cz.muni.fi.scheduler.resources.HostElement;
import cz.muni.fi.xml.pools.ClusterXmlPool;
import cz.muni.fi.xml.pools.DatastoreXmlPool;
import cz.muni.fi.xml.pools.HostXmlPool;
import cz.muni.fi.xml.pools.UserXmlPool;
import cz.muni.fi.xml.pools.VmXmlPool;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 *
 * @author Gabriela Podolnikova
 */
public class ManagerXML implements IManager {
    
    private IVmPool vmPool;
            
    private IHostPool hostPool;
    
    private IUserPool userPool;
    
    private IClusterPool clusterPool;
    
    private IDatastorePool dsPool;

    public ManagerXML(String hostPoolPath, String clusterPoolPath, String userPoolPath, String vmPoolPath, String datastorePoolPath) throws IOException {
        XmlMapper xmlMapper = new XmlMapper();
        String hostPoolMessage = new String(Files.readAllBytes(Paths.get(hostPoolPath)));
        hostPool = xmlMapper.readValue(hostPoolMessage, HostXmlPool.class);
        
        String clusterPoolMessage = new String(Files.readAllBytes(Paths.get(clusterPoolPath)));
        clusterPool = xmlMapper.readValue(clusterPoolMessage, ClusterXmlPool.class);
        
        String userPoolMessage = new String(Files.readAllBytes(Paths.get(userPoolPath)));
        userPool = xmlMapper.readValue(userPoolMessage, UserXmlPool.class);
        
        String vmPoolMessage = new String(Files.readAllBytes(Paths.get(vmPoolPath)));
        vmPool = xmlMapper.readValue(vmPoolMessage, VmXmlPool.class);
        
        String dsPoolMessage = new String(Files.readAllBytes(Paths.get(datastorePoolPath)));
        dsPool = xmlMapper.readValue(dsPoolMessage, DatastoreXmlPool.class);
    }


    @Override
    public IAuthorizationManager getAuthorizationManager() {
        return new AuthorizationManagerXml(hostPool);
    }

    @Override
    public IClusterPool getClusterPool() {
        return clusterPool;
    }

    @Override
    public IDatastorePool getDatastorePool() {
        return dsPool;
    }

    @Override
    public IHostPool getHostPool() {
        return hostPool;
    }

    @Override
    public IUserPool getUserPool() {
        return userPool;
    }

    @Override
    public IVmPool getVmPool() {
        return vmPool;
    }
    
}
