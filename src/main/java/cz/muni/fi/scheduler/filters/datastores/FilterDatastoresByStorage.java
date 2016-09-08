package cz.muni.fi.scheduler.filters.datastores;

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
public class FilterDatastoresByStorage implements IDatastoreFilter {

    @Override
    public boolean test(VmElement vm, DatastoreElement ds, HostElement host, SchedulerData schedulerData) {
        Map<HostElement, List<DatastoreNode>> datastoreNodeStorageCapacity = schedulerData.getDatastoreNodeStorageCapacity();
        Map<DatastoreElement, Integer> datastoreStorageCapacity = schedulerData.getDatastoreStorageCapacity();
        List<HostElement> hosts = schedulerData.getHostPool().getActiveHosts();
        int sizeValue = vm.getDiskSizes();
        boolean matched = false;
        if (ds.isShared()) {
            if (!ds.isMonitored()) {
                return false;
            } else if (vm.isResched()) {
                return true;
            } else {
                //test capacity on ds directly
                System.out.println("Filtering ds by free memory: " + datastoreStorageCapacity.get(ds) + " checking " + sizeValue);
                matched = (datastoreStorageCapacity.get(ds) > sizeValue);
            }
        } else {
            //test ds capacity on host datastores or disks
            //TODO:
            System.out.println("Filtering Host's ds by free memory: " + datastoreNodeStorageCapacity.get(host) + " checking " + sizeValue);
            List<DatastoreNode> datastores = datastoreNodeStorageCapacity.get(host);
            for (DatastoreNode dsNode: datastores) {
                if (Objects.equals(ds.getId(), dsNode.getId_ds())) {
                    matched = dsNode.getFree_mb() > sizeValue;
                }
            }
        }
        return matched;
    }
}
