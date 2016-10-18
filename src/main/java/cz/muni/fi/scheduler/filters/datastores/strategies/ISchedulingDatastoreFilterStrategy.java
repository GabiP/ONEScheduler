/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.muni.fi.scheduler.filters.datastores.strategies;

import cz.muni.fi.scheduler.SchedulerData;
import cz.muni.fi.scheduler.resources.DatastoreElement;
import cz.muni.fi.scheduler.resources.HostElement;
import cz.muni.fi.scheduler.resources.VmElement;

/**
 *
 * @author Gabriela Podolnikova
 */
public interface ISchedulingDatastoreFilterStrategy {

    public boolean test(VmElement vm, DatastoreElement ds, HostElement host, SchedulerData schedulerData);
}
