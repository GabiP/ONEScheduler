package cz.muni.fi.scheduler.filters.hosts.strategies;

import cz.muni.fi.scheduler.elements.HostElement;
import cz.muni.fi.scheduler.elements.VmElement;
import cz.muni.fi.scheduler.elements.nodes.PciNode;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Checks whether the given VM and Host has matching PCI devices.
 * If yes, the VM can be deployed.
 * 
 * @author Gabriela Podolnikova
 */
public class FilterHostByPci implements IHostFilterStrategy {

    protected final Logger LOG = LoggerFactory.getLogger(getClass());
    
    @Override
    public boolean test(VmElement vm, HostElement host) {
        boolean pciFits = true;
        List<PciNode> pcis = host.getPcis();
        if (vm.getPcis().isEmpty()) {
            LOG.info("Pci test invalid");
            return true;
        }
        if (pcis.isEmpty() && !vm.getPcis().isEmpty()) {
            LOG.info("Pci test invalid");
            return false;
        }
        for (PciNode pciVm: vm.getPcis()) {
            boolean foundPciMatch = false;
            for (PciNode pci: pcis) {
                if ((pci.getPci_class().equals(pciVm.getPci_class())) && (pci.getDevice().equals(pciVm.getDevice())) && (pci.getVendor().equals(pciVm.getVendor()))) {
                    foundPciMatch = true;
                    break;
                }
            }
            pciFits = pciFits && foundPciMatch;
        }
        return pciFits;
    }
    
}
