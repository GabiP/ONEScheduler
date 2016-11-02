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
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Checks whether the given Datastore satisfies the given VM's requirements.
 * For example: if the specified datastore is the datastore that the VM requires.
 * 
 * @author Gabriela Podolnikova
 */
public class FilterDatastoresBySchedulingRequirements implements IDatastoreFilterStrategy {
    
    protected final Logger LOG = LoggerFactory.getLogger(getClass());

    @Override
    public boolean test(VmElement vm, DatastoreElement ds, HostElement host){
        if (vm.getSchedDsRequirements() == null) {
            LOG.info("Vm does not have any datastore requirements");
            return true;
        }
        if (vm.getSchedDsRequirements().equals("")) {
            LOG.info("Vm does not have any datastore requirements");
            return true;
        }
        String[] reqs = vm.getSchedDsRequirements().split("\\|");
        boolean fits = false;
        for (String req: reqs) {
            req = req.trim();
            if (req.contains("ID")) {
                Integer id = Integer.parseInt(req.substring(req.indexOf("=")+2, req.length()-1));
                if (Objects.equals(ds.getId(), id)) {
                    fits = true;
                } else {
                    
                }
            }
            if (req.contains("NAME")) {
                String name = req.substring(req.indexOf("=")+2, req.length()-1);
                if (Objects.equals(ds.getName(), name)) {
                    fits = true;
                }
            }
        }
        return fits;
    }
}
