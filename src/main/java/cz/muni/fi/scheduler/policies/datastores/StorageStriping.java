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
 * Striping policy for selecting datastore.
 * Target: Maximize the I/O available to VMs
 * Heuristic: Spread the VMs in the system datastores
 * Implementation: Use those datastores with more free space first
 *
 * @author Gabriela Podolnikova
 */
public class StorageStriping implements IStoragePolicy {

    @Override
    public List<DatastoreElement> sortDatastores(List<DatastoreElement> datastores, VmElement vm, SchedulerData schedulerData) {
        List<DatastoreElement> result = new ArrayList<>();
        DatastoreElement moreFreeSpace = datastores.get(0);
        for (DatastoreElement ds: datastores) {
            if (ds.getFree_mb() > moreFreeSpace.getFree_mb()) {
                moreFreeSpace = ds;
            }
        }
        return result;
    }
    
}
