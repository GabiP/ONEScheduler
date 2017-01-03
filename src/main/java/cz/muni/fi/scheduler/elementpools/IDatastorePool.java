package cz.muni.fi.scheduler.elementpools;

import cz.muni.fi.scheduler.elements.DatastoreElement;
import java.util.List;

/**
 * A pool interface.
 * @author Gabriela Podolnikova
 */
public interface IDatastorePool {
    
    List<DatastoreElement> getDatastores();
    
    DatastoreElement getDatastore(int id);
    
    List<DatastoreElement> getSystemDs();
    
    List<DatastoreElement> getImageDs();
    
    List<Integer> getDatastoresIds();
}
