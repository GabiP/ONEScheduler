package cz.muni.fi.one.pools;

import cz.muni.fi.one.mappers.DatastoreMapper;
import cz.muni.fi.scheduler.elementpools.IDatastorePool;
import cz.muni.fi.scheduler.resources.DatastoreElement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.opennebula.client.Client;
import org.opennebula.client.OneResponse;
import org.opennebula.client.datastore.Datastore;
import org.opennebula.client.datastore.DatastorePool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class represents OpenNebula's DatastorePool containing all instances of datastores in the system.
 * The pool is accessed through OpenNebula's Client. The Client represents the connection with the core of OpenNebula.
 * Each OpenNebula's instance of Datastore is mapped to our DatastoreElement.
 * 
 * @author Gabriela Podolnikova
 */
public class DatastoreElementPool implements IDatastorePool {
    
    protected final Logger log = LoggerFactory.getLogger(getClass());
    
    private final DatastorePool dp;
    
    public DatastoreElementPool(Client oneClient) {
        dp = new DatastorePool(oneClient);
    }

    /**
     * Goes through the pool and maps all datastores.
     * @return the datastores
     */
    @Override
    public List<DatastoreElement> getDatastores() {
        List<DatastoreElement> datastores = new ArrayList<>();
        OneResponse dpr = dp.info();
        if (dpr.isError()) {
            log.error(dpr.getErrorMessage());
        }
        Iterator<Datastore> itr = dp.iterator();
        while (itr.hasNext()) {
            Datastore element = itr.next();
            DatastoreElement d = DatastoreMapper.map(element);
            datastores.add(d);
        }
        return datastores;
    }
    
    /**
     * Gets the DatastoreElement with specified id.
     * @param id to get the desired DatastoreElement
     * @return the DatastoreElement with the specified id
     */
    @Override
    public DatastoreElement getDatastore(int id) {
        dp.info();
        return DatastoreMapper.map(dp.getById(id));
    }
    
    /**
     * Gets only the system datastores from the pool.
     * @return the lsit of system DatastoreElements.
     */
    @Override
    public List<DatastoreElement> getSystemDs() {
        List<DatastoreElement> systemDs = new ArrayList<>();
        for (DatastoreElement ds: getDatastores()) {
            if (ds.getType() == 1) {
                systemDs.add(ds);
            }
        }
        return systemDs;
    }

    /**
     * Gets the datastore's ids.
     * @return the datastoresIds
     */
    @Override
    public List<Integer> getDatastoresIds() {
        List<Integer> dsIds = new ArrayList<>();
        for (DatastoreElement ds: getDatastores()) {
            dsIds.add(ds.getId());
        }
        return dsIds;
    }
}
