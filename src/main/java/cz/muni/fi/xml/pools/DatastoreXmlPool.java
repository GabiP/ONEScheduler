/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.xml.pools;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import cz.muni.fi.scheduler.elementpools.IDatastorePool;
import cz.muni.fi.scheduler.resources.DatastoreElement;
import cz.muni.fi.xml.mappers.DatastoreXmlMapper;
import cz.muni.fi.xml.resources.DatastoreXml;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Gabriela Podolnikova
 */
@JacksonXmlRootElement(localName = "DATASTOREPOOL")
public class DatastoreXmlPool implements IDatastorePool {
    
    @JacksonXmlProperty(localName = "DATASTORE")
    @JacksonXmlElementWrapper(useWrapping = false)    
    private List<DatastoreXml> datastores;        
                    
    @Override
    public List<DatastoreElement> getDatastores() {
        return Collections.unmodifiableList(DatastoreXmlMapper.map(datastores));
    }

    @Override
    public DatastoreElement getDatastore(int id) {
        for (DatastoreElement ds : DatastoreXmlMapper.map(datastores)) {
            if (ds.getId() == id) {
                return ds;
            }
        }
        return null;
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

    @Override
    public List<Integer> getDatastoresIds() {
        List<Integer> dsIds = new ArrayList<>();
        for (DatastoreElement ds: getDatastores()) {
            dsIds.add(ds.getId());
        }
        return dsIds;
    }
    
}
