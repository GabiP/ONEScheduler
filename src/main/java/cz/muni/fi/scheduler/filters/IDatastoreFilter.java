/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.muni.fi.scheduler.filters;

import cz.muni.fi.scheduler.SchedulerData;
import cz.muni.fi.scheduler.resources.DatastoreElement;
import cz.muni.fi.scheduler.resources.VmElement;

/**
 * Filters are used for filtering datastores in the system.
 * It matches the given Virtual machine and datastore, whether it meets the desired criteria.
 * For each criteria we create one Filter class by impelenting this interface.
 * 
 * @author Gabriela Podolnikova
 */
public interface IDatastoreFilter {

    public boolean test(VmElement vm, DatastoreElement ds, SchedulerData schedulerData);
}
