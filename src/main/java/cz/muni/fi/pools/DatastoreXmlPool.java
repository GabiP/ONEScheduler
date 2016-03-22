/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.pools;

import cz.muni.fi.resources.DatastoreXml;
import java.util.ArrayList;
import java.util.Iterator;
import org.opennebula.client.Client;
import org.opennebula.client.OneResponse;
import org.opennebula.client.datastore.Datastore;
import org.opennebula.client.datastore.DatastorePool;

/**
 *
 * @author Gabriela Podolnikova
 */
public class DatastoreXmlPool {
    
    private DatastorePool dp;
    
    private ArrayList<DatastoreXml> datastores;
    
    public ArrayList<DatastoreXml> loadDatastores(Client oneClient) {
        datastores = new ArrayList<>();
        dp = new DatastorePool(oneClient);
        OneResponse dpr = dp.info();
        if (dpr.isError()) {
            //TODO: log it
            System.out.println(dpr.getErrorMessage());
        }
        Iterator<Datastore> itr = dp.iterator();
        while (itr.hasNext()) {
            Datastore element = itr.next();
            System.out.println("Datastore: " + element);
            DatastoreXml d = new DatastoreXml(element);
            System.out.println("Datastore: " + d);
            datastores.add(d);
        }
        return datastores;
    }

    /**
     * @return the datastores
     */
    public ArrayList<DatastoreXml> getDatastores() {
        return datastores;
    }

    /**
     * @param datastores the datastores to set
     */
    public void setDatastores(ArrayList<DatastoreXml> datastores) {
        this.datastores = datastores;
    }
}
