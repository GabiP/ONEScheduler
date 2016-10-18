package cz.muni.fi.scheduler.filters.hosts.strategies;

import cz.muni.fi.scheduler.SchedulerData;
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
public class FilterHostByPci implements IHostFilterStrategy {

    @Override
    public boolean test(VmElement vm, HostElement host) {
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
