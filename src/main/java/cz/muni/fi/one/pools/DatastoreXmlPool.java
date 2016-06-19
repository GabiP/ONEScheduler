/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.one.pools;

import cz.muni.fi.scheduler.resources.DatastoreElement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
import org.opennebula.client.Client;
import org.opennebula.client.OneResponse;
import org.opennebula.client.datastore.Datastore;
import org.opennebula.client.datastore.DatastorePool;

/**
 *
 * @author Gabriela Podolnikova
 */
public class DatastoreXmlPool {
    
    private final DatastorePool dp;
    
    private ArrayList<DatastoreElement> datastores;
    
    private ArrayList<Integer> datastoresIds;
    
    public DatastoreXmlPool(Client oneClient) {
        dp = new DatastorePool(oneClient);
    }
    
    public void loadDatastores() {
        datastores = new ArrayList<>();
        datastoresIds = new ArrayList<>();
        OneResponse dpr = dp.info();
        if (dpr.isError()) {
            //TODO: log it
            System.out.println(dpr.getErrorMessage());
        }
        Iterator<Datastore> itr = dp.iterator();
        while (itr.hasNext()) {
            Datastore element = itr.next();
            DatastoreElement d = new DatastoreElement(element);
            datastores.add(d);
            datastoresIds.add(d.getId());
        }
    }

    /**
     * @return the datastores
     */
    public ArrayList<DatastoreElement> getDatastores() {
        return datastores;
    }

    /**
     * @param datastores the datastores to set
     */
    public void setDatastores(ArrayList<DatastoreElement> datastores) {
        this.datastores = datastores;
    }
    
    public DatastoreElement getById(Integer id) {
        for (DatastoreElement ds: datastores) {
            if (Objects.equals(ds.getId(), id)) {
                return ds;
            }
        }
        return null;
    }
    
    public ArrayList<Integer> getSystemDs() {
        ArrayList<Integer> systemDs = new ArrayList<>();
        for (DatastoreElement ds: datastores) {
            if (ds.getType() == 1) {
                systemDs.add(ds.getId());
            }
        }
        return systemDs;
    }

    /**
     * @return the datastoresIds
     */
    public ArrayList<Integer> getDatastoresIds() {
        return datastoresIds;
    }
}
