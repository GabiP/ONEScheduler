/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.scheduler.policies.datastores;

import cz.muni.fi.scheduler.RankPair;
import cz.muni.fi.scheduler.SchedulerData;
import cz.muni.fi.scheduler.resources.DatastoreElement;
import cz.muni.fi.scheduler.resources.HostElement;
import cz.muni.fi.scheduler.resources.VmElement;
import cz.muni.fi.scheduler.resources.nodes.DatastoreNode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
    public RankPair selectDatastore(List<DatastoreElement> datastores, HostElement host, SchedulerData schedulerData) {
        Map<HostElement, List<DatastoreNode>> datastoreNodeStorageCapacity = schedulerData.getDatastoreNodeStorageCapacity();
        List<DatastoreNode> datastoreNodes = datastoreNodeStorageCapacity.get(host);
        Map<DatastoreElement, Integer> datastoreStorageCapacity = schedulerData.getDatastoreStorageCapacity();
        Integer lessFreeSpace = Integer.MAX_VALUE;
        Integer capacity;
        DatastoreElement result = null;
        for (DatastoreElement ds : datastores) {
            if (ds.isShared()) {
                capacity = datastoreStorageCapacity.get(ds);
                if(capacity < lessFreeSpace) {
                    result = ds;
                    lessFreeSpace = capacity;
                }
            } else {
                for (DatastoreNode dsNode : datastoreNodes) {
                    if (ds.getId().intValue() == dsNode.getId_ds().intValue()) {
                        capacity = dsNode.getFree_mb();
                        if (capacity < lessFreeSpace) {
                            result = ds;
                            lessFreeSpace = capacity;
                        }
                    }
                }
            }
        }
        return new RankPair(result, lessFreeSpace);
    }
    
    @Override    
    public DatastoreElement getBestRankedDatastore(List<RankPair> values) {
        RankPair best = values.get(0);
        for (RankPair pair: values) {
            if (pair.getRank() < best.getRank()) {
                best = pair;
            }
        }
        return best.getDs();
    }
}
