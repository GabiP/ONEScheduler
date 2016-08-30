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
 * This class provides access to xml files that hold the data necessary for scheduling.
 * 
 * @author Gabriela Podolnikova
 */
public class ManagerXML implements IManager {
    
    private String hostPoolPath;
    private String clusterPoolPath;
    private String userPoolPath;
    private String vmPoolPath;
    private String datastorePoolPath;
    
    /**
     * Creates all pools necessary for scheduling by reading the xmls.
     * @param hostPoolPath the path to hostpoool.xml
     * @param clusterPoolPath the path to clusterpool.xml
     * @param userPoolPath the path to userpool.xml
     * @param vmPoolPath the path to vmpool.xml
     * @param datastorePoolPath the path to datastorepool.xml
     * @throws IOException 
     */
    public ManagerXML(String hostPoolPath, String clusterPoolPath, String userPoolPath, String vmPoolPath, String datastorePoolPath) {
        this.hostPoolPath = hostPoolPath;
        this.clusterPoolPath = clusterPoolPath;
        this.userPoolPath = userPoolPath;
        this.vmPoolPath = vmPoolPath;
        this.datastorePoolPath = datastorePoolPath;
    }


    @Override
    public IAuthorizationManager getAuthorizationManager() throws IOException{
        return new AuthorizationManagerXml(getHostPool());
    }

    @Override
    public IClusterPool getClusterPool() throws IOException {
        XmlMapper xmlMapper = new XmlMapper();
        String clusterPoolMessage = new String(Files.readAllBytes(Paths.get(clusterPoolPath)));
        return xmlMapper.readValue(clusterPoolMessage, ClusterXmlPool.class);
    }

    @Override
    public IDatastorePool getDatastorePool() throws IOException {
        XmlMapper xmlMapper = new XmlMapper();
        String dsPoolMessage = new String(Files.readAllBytes(Paths.get(datastorePoolPath)));
        return xmlMapper.readValue(dsPoolMessage, DatastoreXmlPool.class);
    }

    @Override
    public IHostPool getHostPool() throws IOException {
        XmlMapper xmlMapper = new XmlMapper();
        String hostPoolMessage = new String(Files.readAllBytes(Paths.get(hostPoolPath)));
        return xmlMapper.readValue(hostPoolMessage, HostXmlPool.class);
    }

    @Override
    public IUserPool getUserPool() throws IOException {
        XmlMapper xmlMapper = new XmlMapper();
        String userPoolMessage = new String(Files.readAllBytes(Paths.get(userPoolPath)));
        return xmlMapper.readValue(userPoolMessage, UserXmlPool.class);
    }

    @Override
    public IVmPool getVmPool() throws IOException {
        XmlMapper xmlMapper = new XmlMapper();
        String vmPoolMessage = new String(Files.readAllBytes(Paths.get(vmPoolPath)));
        return xmlMapper.readValue(vmPoolMessage, VmXmlPool.class);
    }
    
}
