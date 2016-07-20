package cz.muni.fi.scheduler.filters;

import cz.muni.fi.scheduler.Scheduler;
import cz.muni.fi.scheduler.elementpools.IClusterPool;
import cz.muni.fi.scheduler.elementpools.IDatastorePool;
import cz.muni.fi.scheduler.resources.HostElement;
import cz.muni.fi.scheduler.resources.VmElement;
import java.util.Objects;

/**
 * Checks whether the given Host satisfies the given VM's requirements.
 * For example: if the specified host is the host that the VM requires.
 * 
 * @author Gabriela Podolnikova
 */
public class FilterVmBySchedulingRequirements implements IFilter {

    @Override
    public boolean test(VmElement vm, HostElement host, IClusterPool clusterPool, IDatastorePool dsPool, Scheduler scheduler) {
        if (vm.getSchedRequirements() == null) {
            return true;
        }
        if (vm.getSchedRequirements().equals("")) {
            System.out.println("Vm does not have any requirements");
            return true;
        }
        String[] reqs = vm.getSchedRequirements().split("\\|");
        boolean fits = false;
        for (String req: reqs) {
            req = req.trim();
            Integer id = Integer.parseInt(req.substring(req.indexOf("=")+2, req.length()-1));
            if (req.contains("ID")) {
                if (Objects.equals(host.getId(), id)) {
                    fits = true;
                }
            }
            if (req.contains("CLUSTER")) {
                if (Objects.equals(host.getClusterId(), id)) {
                    fits = true;
                }
            }
        }
        return fits;
    }
    
}
