/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.scheduler.policies.datastores;

import cz.muni.fi.scheduler.SchedulerData;
import cz.muni.fi.scheduler.resources.DatastoreElement;
import cz.muni.fi.scheduler.resources.VmElement;
import java.util.ArrayList;
import java.util.List;

/**
 * Packing policy for selecting datastore.
 * Tries to optimize storage usage by selecting the DS with less free space
 * Target: Minimize the number of system datastores in use
 * Heuristic: Pack the VMs in the system datastores to reduce VM fragmentation
 * Implementation: Use those datastores with less free space first
 * 
 * @author Gabriela Podolnikova
 */
public class StoragePacking implements IStoragePolicy {

    @Override
    public List<DatastoreElement> sortDatastores(List<DatastoreElement> datastores, VmElement vm, SchedulerData schedulerData) {
        List<DatastoreElement> result = new ArrayList<>();
        DatastoreElement lessFreeSpace = datastores.get(0);
        for (DatastoreElement ds: datastores) {
            if (ds.getFree_mb() < lessFreeSpace.getFree_mb()) {
                lessFreeSpace = ds;
            }
        }
        return result;
    }
    
}
