/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.scheduler.policies.datastores;

import cz.muni.fi.scheduler.SchedulerData;
import cz.muni.fi.scheduler.resources.DatastoreElement;
import cz.muni.fi.scheduler.resources.VmElement;
import java.util.List;

/**
 * This class represents an interface that all storage policies should be impelementing.
 * 
 * @author Gabriela Podolnikova
 */
public interface IStoragePolicy {
    
    public List<DatastoreElement> sortDatastores(List<DatastoreElement> datastores, VmElement vm, SchedulerData schedulerData);
    
}
