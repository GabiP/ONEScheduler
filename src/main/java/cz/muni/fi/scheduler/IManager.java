package cz.muni.fi.scheduler;

import cz.muni.fi.authorization.IAuthorizationManager;
import cz.muni.fi.scheduler.elementpools.IClusterPool;
import cz.muni.fi.scheduler.elementpools.IDatastorePool;
import cz.muni.fi.scheduler.elementpools.IHostPool;
import cz.muni.fi.scheduler.elementpools.IUserPool;
import cz.muni.fi.scheduler.elementpools.IVmPool;

/**
 *
 * @author Gabriela Podolnikova
 */
public interface IManager {
    
    public IAuthorizationManager getAuthorizationManager();
    public IClusterPool getClusterPool();
    public IDatastorePool getDatastorePool();
    public IHostPool getHostPool();
    public IUserPool getUserPool();
    public IVmPool getVmPool();
}
