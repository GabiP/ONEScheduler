package cz.muni.fi.scheduler;

import cz.muni.fi.authorization.IAuthorizationManager;
import cz.muni.fi.scheduler.elementpools.IClusterPool;
import cz.muni.fi.scheduler.elementpools.IDatastorePool;
import cz.muni.fi.scheduler.elementpools.IHostPool;
import cz.muni.fi.scheduler.elementpools.IUserPool;
import cz.muni.fi.scheduler.elementpools.IVmPool;
import java.io.IOException;

/**
 *
 * @author Gabriela Podolnikova
 */
public interface IManager {
    
    public IAuthorizationManager getAuthorizationManager() throws IOException;
    public IClusterPool getClusterPool() throws IOException;
    public IDatastorePool getDatastorePool() throws IOException;
    public IHostPool getHostPool() throws IOException;
    public IUserPool getUserPool() throws IOException;
    public IVmPool getVmPool() throws IOException;
}
