/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.muni.fi.scheduler.filters.datastores;

import cz.muni.fi.scheduler.resources.DatastoreElement;
import cz.muni.fi.scheduler.resources.HostElement;
import cz.muni.fi.scheduler.resources.VmElement;
import java.util.List;

/**
 *
 * @author Gabriela Podolnikova
 */
public interface IDatastoreFilter {

    public List<DatastoreElement> filterDatastores(List<DatastoreElement> datastores, HostElement host, VmElement vm);
}
