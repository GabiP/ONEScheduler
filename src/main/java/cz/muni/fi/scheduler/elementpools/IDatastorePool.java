/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.scheduler.elementpools;

import cz.muni.fi.scheduler.resources.DatastoreXml;
import java.util.List;

/**
 *
 * @author Gabriela Podolnikova
 */
public interface IDatastorePool {
    
    public List<DatastoreXml> getDatastores();
    
    public DatastoreXml getDatastore(int id);
    
    public List<DatastoreXml> getSystemDs();
    
    public List<Integer> getDatastoresIds();
}
