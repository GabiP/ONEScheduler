package cz.muni.fi.scheduler.policies.datastores;

import cz.muni.fi.scheduler.core.Match;
import cz.muni.fi.scheduler.core.RankPair;
import cz.muni.fi.scheduler.core.SchedulerData;
import cz.muni.fi.scheduler.elements.DatastoreElement;
import cz.muni.fi.scheduler.elements.HostElement;
import cz.muni.fi.scheduler.elements.nodes.DatastoreNode;
import java.util.List;
import java.util.Map;

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
        Integer lessFreeSpace = Integer.MAX_VALUE;
        Integer capacity;
        DatastoreElement result = null;
        for (DatastoreElement ds : datastores) {
            if (ds.isShared()) {
                Integer reservedStorage = schedulerData.getReservedStorage(ds);
                capacity = ds.getFree_mb() - reservedStorage;
                if (capacity < lessFreeSpace) {
                    result = ds;
                    lessFreeSpace = capacity;
                }
            } else {
                DatastoreNode dsNode = host.getDatastoreNode(ds.getId());
                Integer reservedStorage = schedulerData.getReservedStorage(host, ds);
                if (dsNode != null) {
                    capacity = dsNode.getFree_mb() - reservedStorage;
                    if (capacity < lessFreeSpace) {
                        result = ds;
                        lessFreeSpace = capacity;
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
    
    public Match getBestRankedDatastore(Map<HostElement, RankPair> candidates) {
        Match match = new Match();
        Integer bestRank = Integer.MAX_VALUE;
        for(Map.Entry<HostElement, RankPair> entry: candidates.entrySet()) {
            Integer rank = entry.getValue().getRank();
            if (rank < bestRank) {
                bestRank = rank;
                match.setHost(entry.getKey());
                match.setDatastore(entry.getValue().getDs());
            }
        }
        return match;
    }
}
