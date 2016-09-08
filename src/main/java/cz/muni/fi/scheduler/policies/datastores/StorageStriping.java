/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.scheduler.policies.datastores;

import cz.muni.fi.scheduler.SchedulerData;
import cz.muni.fi.scheduler.resources.DatastoreElement;
import cz.muni.fi.scheduler.resources.HostElement;
import cz.muni.fi.scheduler.resources.VmElement;
import cz.muni.fi.scheduler.resources.nodes.DatastoreNode;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
    public DatastoreElement selectDatastore(List<DatastoreElement> datastores, HostElement host, SchedulerData schedulerData) {
        Map<HostElement, List<DatastoreNode>> datastoreNodeStorageCapacity = schedulerData.getDatastoreNodeStorageCapacity();
        List<DatastoreNode> datastoreNodes = datastoreNodeStorageCapacity.get(host);
        Map<DatastoreElement, Integer> datastoreStorageCapacity = schedulerData.getDatastoreStorageCapacity();
        Integer moreFreeSpace = Integer.MIN_VALUE;
        Integer capacity;
        DatastoreElement result = null;
        for (DatastoreElement ds : datastores) {
            if (ds.isShared()) {
                capacity = datastoreStorageCapacity.get(ds);
                if(capacity > moreFreeSpace) {
                    result = ds;
                    moreFreeSpace = capacity;
                }
            } else {
                for (DatastoreNode dsNode : datastoreNodes) {
                    if (ds.getId().intValue() == dsNode.getId_ds().intValue()) {
                        capacity = dsNode.getFree_mb();
                        if (capacity > moreFreeSpace) {
                            result = ds;
                            moreFreeSpace = capacity;
                        }
                    }
                }
            }
        }
        return result;
    }
}
