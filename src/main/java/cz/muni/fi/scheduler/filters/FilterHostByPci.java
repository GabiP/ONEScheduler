package cz.muni.fi.scheduler.filters;

import cz.muni.fi.scheduler.Scheduler;
import cz.muni.fi.scheduler.elementpools.IClusterPool;
import cz.muni.fi.scheduler.elementpools.IDatastorePool;
import cz.muni.fi.scheduler.resources.HostElement;
import cz.muni.fi.scheduler.resources.VmElement;
import cz.muni.fi.scheduler.resources.nodes.PciNode;
import java.util.List;

/**
 * Checks whether the given VM and Host has matching PCI devices.
 * If yes, the VM can be deployed.
 * 
 * @author Gabriela Podolnikova
 */
public class FilterHostByPci implements IFilter {

    @Override
    public boolean test(VmElement vm, HostElement host, IClusterPool clusterPool, IDatastorePool dsPool, Scheduler scheduler) {
        boolean pciFits = false;
        List<PciNode> pcis = host.getPcis();
        if (pcis.isEmpty() && vm.getPcis().isEmpty()) {
            return true;
        }
        if (!pcis.isEmpty() && vm.getPcis().isEmpty()) {
            return true;
        }
        if (pcis.isEmpty() && !vm.getPcis().isEmpty()) {
            return false;
        }
        for (PciNode pci: pcis) {
            for (PciNode pciVm: vm.getPcis()) {
                if ((pci.getPci_class().equals(pciVm.getPci_class())) && (pci.getDevice().equals(pciVm.getDevice())) && (pci.getVendor().equals(pciVm.getVendor()))) {
                    pciFits = true;
                }
            }
        }
        return pciFits;
    }
    
}
