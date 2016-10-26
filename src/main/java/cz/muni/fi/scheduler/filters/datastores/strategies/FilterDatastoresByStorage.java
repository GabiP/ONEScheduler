package cz.muni.fi.scheduler.filters.datastores.strategies;

import cz.muni.fi.scheduler.SchedulerData;
import cz.muni.fi.scheduler.resources.DatastoreElement;
import cz.muni.fi.scheduler.resources.HostElement;
import cz.muni.fi.scheduler.resources.VmElement;
import cz.muni.fi.scheduler.resources.nodes.DatastoreNode;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Tests whether current host has enough free space(mb) in datastores to host
 * the specified vm disks. Simplified version: firstly it adds up the sizes(mb)
 * of vm's disks. Then this value is checked on cluster's datastores.
 * TODO: It can divide the sizes of disks and try if it will fit somehow on the available
 * datastores.
 *
 * @author Gabriela Podolnikova
 */
public class FilterDatastoresByStorage implements ISchedulingDatastoreFilterStrategy {

    @Override
    public boolean test(VmElement vm, DatastoreElement ds, HostElement host, SchedulerData schedulerData) {
        //check if ds and host have the same clusterId
        if (!(ds.getClusters().contains(host.getClusterId()))) {
            System.out.println("Filtering DS by storage. The DS and HOST ids don't match");
            return false;
        }
        //get the reserved storage for the datastore we are checking
        Integer reservedStorage = schedulerData.getReservedStorage(ds);
        int sizeValue = vm.getDiskSizes();
        boolean matched = false;
        if (ds.isShared()) {
            if (!ds.isMonitored()) {
                return false;
            } else if (vm.isResched()) {
                return true;
            } else {
                //test capacity on ds directly
                matched = testOnDs(sizeValue, reservedStorage, ds.getFree_mb());
            }
        } else {
            //test ds capacity on host datastores or disks
            List<DatastoreNode> datastores = host.getDatastores();
            if (datastores.isEmpty()) {
                matched = testOnHost(sizeValue, reservedStorage, host.getFree_disk());
            } else {
                matched = testOnDsNode(sizeValue, reservedStorage, datastores, ds.getId());
            }
        }
        return matched;
    }
    
    private boolean testOnDs(Integer sizeValue, Integer reservedStorage, Integer freeSpace) {
        Integer actualStorage = freeSpace - reservedStorage;
        System.out.println("Filtering ds by free memory: " + actualStorage + " checking " + sizeValue);
        return (actualStorage > sizeValue);
    }
    
    private boolean testOnDsNode(Integer sizeValue, Integer reservedStorage, List<DatastoreNode> datastores, Integer dsId) {
        boolean matched = false;
        for (DatastoreNode dsNode : datastores) {
            if (Objects.equals(dsId, dsNode.getId_ds())) {
                Integer actualStorage = dsNode.getFree_mb() - reservedStorage;
                System.out.println("Filtering Host's ds by free memory: " + actualStorage + " checking " + sizeValue);
                matched = (actualStorage > sizeValue);
            }
        }
        return matched;
    }
    
    private boolean testOnHost(Integer sizeValue, Integer reservedStorage, Integer freeSpace) {
        Integer actualStorage = freeSpace - reservedStorage;
        return (actualStorage > sizeValue);
    }
}
