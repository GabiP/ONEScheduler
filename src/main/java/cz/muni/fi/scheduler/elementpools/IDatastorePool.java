/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.scheduler.elementpools;

import cz.muni.fi.scheduler.resources.DatastoreElement;
import java.util.List;

/**
 *
 * @author Gabriela Podolnikova
 */
public interface IDatastorePool {
    
    public List<DatastoreElement> getDatastores();
    
    public DatastoreElement getDatastore(int id);
    
    public List<DatastoreElement> getSystemDs();
    
    public List<DatastoreElement> getImageDs();
    
    public List<Integer> getDatastoresIds();
}
