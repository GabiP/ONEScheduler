package cz.muni.fi.scheduler;

import cz.muni.fi.authorization.AuthorizationManager;
import cz.muni.fi.authorization.IAuthorizationManager;
import cz.muni.fi.one.pools.AclElementPool;
import cz.muni.fi.one.pools.ClusterElementPool;
import cz.muni.fi.one.pools.DatastoreElementPool;
import cz.muni.fi.one.pools.HostElementPool;
import cz.muni.fi.one.pools.UserElementPool;
import cz.muni.fi.one.pools.VmElementPool;
import cz.muni.fi.scheduler.elementpools.IAclPool;
import cz.muni.fi.scheduler.elementpools.IClusterPool;
import cz.muni.fi.scheduler.elementpools.IDatastorePool;
import cz.muni.fi.scheduler.elementpools.IHostPool;
import cz.muni.fi.scheduler.elementpools.IUserPool;
import cz.muni.fi.scheduler.elementpools.IVmPool;
import java.io.IOException;
import org.opennebula.client.Client;

/**
 * This class creates a connection with OpenNebula by creating the instance of the Client class.
 * The Client class represents the connection with the core and handles the xml-rpc calls.
 * 
 * @author Gabriela Podolnikova
 */
public class ManagerOne implements IManager{
    
    private Client oneClient;
    
    private IVmPool vmPool;
            
    private IHostPool hostPool;
    
    private IUserPool userPool;
    
    private IAclPool aclPool;
    
    private IClusterPool clusterPool;
    
    private IDatastorePool dsPool;
    
    /**
     * Creates a new xml-rpc client with specified options - secret and endpoint.
     * And loads OpenNebula's pools.
     * @param secret A string containing the ONE user:password tuple. Can be null
     * @param endpoint Where the rpc server is listening, must be something like "http://localhost:2633/RPC2". Can be null
     */
    public ManagerOne(String secret, String endpoint) {
        try {           
            oneClient = new Client(secret, endpoint);            
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        try {
            loadOnePools(oneClient);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    public void loadOnePools(Client oneClient) throws IOException {
        vmPool = new VmElementPool(oneClient);
        hostPool = new HostElementPool(oneClient);
        userPool = new UserElementPool(oneClient);
        aclPool = new AclElementPool(oneClient);
        clusterPool = new ClusterElementPool(oneClient);
        dsPool = new DatastoreElementPool(oneClient);
    }

    @Override
    public IAuthorizationManager getAuthorizationManager() {
        return new AuthorizationManager(aclPool, clusterPool, hostPool, dsPool, userPool);
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
