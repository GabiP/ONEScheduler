/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.scheduler.elementpools;

import cz.muni.fi.scheduler.elements.DatastoreElement;
import java.util.List;

/**
 *
 * @author Gabriela Podolnikova
 */
public interface IDatastorePool {
    
    List<DatastoreElement> getDatastores();
    
    DatastoreElement getDatastore(int id);
    
    List<DatastoreElement> getSystemDs();
    
    List<DatastoreElement> getImageDs();
    
    List<Integer> getDatastoresIds();
}
