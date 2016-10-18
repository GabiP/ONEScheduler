/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.muni.fi.scheduler.filters.datastores;

import cz.muni.fi.scheduler.filters.datastores.strategies.IDatastoreFilterStrategy;
import cz.muni.fi.scheduler.filters.datastores.strategies.ISchedulingDatastoreFilterStrategy;
import cz.muni.fi.scheduler.SchedulerData;
import cz.muni.fi.scheduler.elementpools.IHostPool;
import cz.muni.fi.scheduler.filters.hosts.strategies.IHostFilterStrategy;
import cz.muni.fi.scheduler.filters.hosts.strategies.ISchedulingHostFilterStrategy;
import cz.muni.fi.scheduler.resources.DatastoreElement;
import cz.muni.fi.scheduler.resources.HostElement;
import cz.muni.fi.scheduler.resources.VmElement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Gabriela Podolnikova
 */
public class SchedulingDatastoreFilter implements IDatastoreFilter {

    /**
     * The list of filters to be used for matching the datastore for a virtual machine.
     */
    private List<IDatastoreFilterStrategy> datastoreFilters;
    
    private List<ISchedulingDatastoreFilterStrategy> schedulingDatastoreFilters;
        
    private SchedulerData schedulerData;

    public SchedulingDatastoreFilter(List<IDatastoreFilterStrategy> datastoreFilters, List<ISchedulingDatastoreFilterStrategy> schedulingDatastoreFilters, SchedulerData schedulerData) {
        this.datastoreFilters = datastoreFilters;
        this.schedulingDatastoreFilters = schedulingDatastoreFilters;
        this.schedulerData = schedulerData;
    }
    
    /**
     * Filter datastores that belongs to the host and can host the vm.
     * @param datastores all system datastores in the system
     * @param host the host for matching the datastore
     * @param vm the vm to be tested
     * @return the list of filtered datastores
     */
    public List<DatastoreElement> filterDatastores(List<DatastoreElement> datastores, HostElement host, VmElement vm) {
        List<DatastoreElement> filteredDatastores = new ArrayList<>();
        for (DatastoreElement ds: datastores) {
            boolean matched = isSuitableDatastore(host, ds, vm);
            if (matched) {
                filteredDatastores.add(ds);
            }
        }
        return filteredDatastores;
    }
    
    /**
     * Goes through all filters and calls the test if the vm and host matches by the specified criteria in the filter.
     * @param host the host to be tested
     * @param ds the datasotre to be tested
     * @param vm the virtual machine to be tested
     * @return true if the host and vm match, false othewise
     */
    public boolean isSuitableDatastore(HostElement host, DatastoreElement ds, VmElement vm) {
         boolean result = true;
         for (IDatastoreFilterStrategy filter: datastoreFilters) {
             result = result && filter.test(vm, ds, host);
         }
         for (ISchedulingDatastoreFilterStrategy filter: schedulingDatastoreFilters) {
             result = result && filter.test(vm, ds, host, schedulerData);
         }
         return result;
     }
}
