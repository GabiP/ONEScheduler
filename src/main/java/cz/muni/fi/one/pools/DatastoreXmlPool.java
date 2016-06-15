/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.one.pools;

import cz.muni.fi.scheduler.elementpools.IDatastorePool;
import cz.muni.fi.scheduler.resources.DatastoreXml;
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
public class DatastoreXmlPool implements IDatastorePool {
    
    private final DatastorePool dp;
    
    public DatastoreXmlPool(Client oneClient) {
        dp = new DatastorePool(oneClient);
    }

    /**
     * @return the datastores
     */
    @Override
    public List<DatastoreXml> getDatastores() {
        List<DatastoreXml> datastores = new ArrayList<>();
        OneResponse dpr = dp.info();
        if (dpr.isError()) {
            //TODO: log it
            System.out.println(dpr.getErrorMessage());
        }
        Iterator<Datastore> itr = dp.iterator();
        while (itr.hasNext()) {
            Datastore element = itr.next();
            DatastoreXml d = new DatastoreXml(element);
            datastores.add(d);
        }
        return datastores;
    }
    
    @Override
    public DatastoreXml getDatastore(int id) {
        return new DatastoreXml(dp.getById(id));
    }
    
    @Override
    public List<DatastoreXml> getSystemDs() {
        List<DatastoreXml> systemDs = new ArrayList<>();
        for (DatastoreXml ds: getDatastores()) {
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
        for (DatastoreXml ds: getDatastores()) {
            dsIds.add(ds.getId());
        }
        return dsIds;
    }
}
