/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.xml.pools;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import cz.muni.fi.scheduler.elementpools.IDatastorePool;
import cz.muni.fi.scheduler.resources.DatastoreElement;
import cz.muni.fi.xml.mappers.DatastoreXmlMapper;
import cz.muni.fi.xml.resources.DatastoreJacksonPool;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Gabriela Podolnikova
 */
public class DatastoreXmlPool implements IDatastorePool {
    
    List<DatastoreElement> datastores;

    public DatastoreXmlPool(String xml) throws IOException {
        XmlMapper xmlMapper = new XmlMapper();
        DatastoreJacksonPool dsPool = xmlMapper.readValue(xml, DatastoreJacksonPool.class);
        datastores = DatastoreXmlMapper.map(dsPool.getDatastores());
    }
    
    @Override
    public List<DatastoreElement> getDatastores() {
        return Collections.unmodifiableList(datastores);
    }

    @Override
    public DatastoreElement getDatastore(int id) {
        for (DatastoreElement ds : datastores) {
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
