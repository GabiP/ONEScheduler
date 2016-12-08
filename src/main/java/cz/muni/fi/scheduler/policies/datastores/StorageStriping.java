package cz.muni.fi.scheduler.policies.datastores;

import cz.muni.fi.scheduler.core.RankPair;
import cz.muni.fi.scheduler.core.SchedulerData;
import cz.muni.fi.scheduler.elements.DatastoreElement;
import cz.muni.fi.scheduler.elements.HostElement;
import cz.muni.fi.scheduler.elements.nodes.DatastoreNode;
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
    public RankPair selectDatastore(List<DatastoreElement> datastores, HostElement host, SchedulerData schedulerData) {
        Integer moreFreeSpace = Integer.MIN_VALUE;
        Integer capacity;
        DatastoreElement result = null;
        for (DatastoreElement ds : datastores) {
            if (ds.isShared()) {
                Integer reservedStorage = schedulerData.getReservedStorage(ds);
                capacity = ds.getFree_mb() - reservedStorage;
                if (capacity > moreFreeSpace) {
                    result = ds;
                    moreFreeSpace = capacity;
                }
            } else {
                DatastoreNode dsNode = host.getDatastoreNode(ds.getId());
                Integer reservedStorage = schedulerData.getReservedStorage(host, ds);
                if (dsNode != null) {
                    capacity = dsNode.getFree_mb() - reservedStorage;
                    if (capacity > moreFreeSpace) {
                        result = ds;
                        moreFreeSpace = capacity;
                    }
                }
            }
        }
        return new RankPair(result, moreFreeSpace);
    }
    
    @Override    
    public DatastoreElement getBestRankedDatastore(List<RankPair> values) {
        RankPair best = values.get(0);
        for (RankPair pair: values) {
            if (pair.getRank() > best.getRank()) {
                best = pair;
            }
        }
        return best.getDs();
    }
}
