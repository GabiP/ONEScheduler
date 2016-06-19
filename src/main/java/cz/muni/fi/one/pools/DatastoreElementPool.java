/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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

/**
 *
 * @author Gabriela Podolnikova
 */
public class DatastoreElementPool implements IDatastorePool {
    
    private final DatastorePool dp;
    
    public DatastoreElementPool(Client oneClient) {
        dp = new DatastorePool(oneClient);
    }

    /**
     * @return the datastores
     */
    @Override
    public List<DatastoreElement> getDatastores() {
        List<DatastoreElement> datastores = new ArrayList<>();
        OneResponse dpr = dp.info();
        if (dpr.isError()) {
            //TODO: log it
            System.out.println(dpr.getErrorMessage());
        }
        Iterator<Datastore> itr = dp.iterator();
        while (itr.hasNext()) {
            Datastore element = itr.next();
            DatastoreElement d = DatastoreMapper.map(element);
            datastores.add(d);
        }
        return datastores;
    }
    
    @Override
    public DatastoreElement getDatastore(int id) {
        return DatastoreMapper.map(dp.getById(id));
    }
    
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
