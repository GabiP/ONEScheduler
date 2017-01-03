package cz.muni.fi.scheduler.authorization;

import cz.muni.fi.scheduler.elementpools.IDatastorePool;
import cz.muni.fi.scheduler.elementpools.IHostPool;
import cz.muni.fi.scheduler.elements.DatastoreElement;
import cz.muni.fi.scheduler.elements.HostElement;
import cz.muni.fi.scheduler.elements.VmElement;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents authorization manager for the testing mode.
 * We do not provide ACLs XML file. Therefore we assume that all users can use all resources.
 * Which means that for every VM we return all active hosts available in the system.
 * 
 * If the ACL authorization is developped in the future, the authorize method in this class will manage the authorization.
 * 
 * @author Gabriela Podolnikova
 */
public class AuthorizationManagerXml implements IAuthorizationManager {
    
    private List<HostElement> hosts = new ArrayList<>();
    private List<DatastoreElement> datastores = new ArrayList<>();

    public AuthorizationManagerXml(IHostPool hostPool, IDatastorePool dsPool) {
        hosts = hostPool.getActiveHosts();
        datastores = dsPool.getSystemDs();
    }
    
    @Override
    public void authorize(List<VmElement> vms) {
    }

    @Override
    public List<HostElement> getAuthorizedHosts(Integer id) {
        return hosts;
    }

    @Override
    public List<DatastoreElement> getAuthorizedDs(Integer id) {
        return datastores;
    }       
}
