package cz.muni.fi.scheduler.filters.hosts.strategies;

import cz.muni.fi.scheduler.resources.HostElement;
import cz.muni.fi.scheduler.resources.VmElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Andras Urge 
 */
public class FilterHostByMaxMemory implements IHostFilterStrategy {
    
    protected final Logger LOG = LoggerFactory.getLogger(getClass());
    
    /**
     * Tests whether the maximum RAM of a host is sufficient for the VM.
     * 
     * @param vm Virtual Machine to be checked
     * @param host Host to be checked
     * @return true if the the maximum RAM of a host is sufficient for the vm
     */
    @Override
    public boolean test(VmElement vm, HostElement host) {
        LOG.info("Filtering: " + vm.getVmId() + " and " + host.getId());
        return host.getMax_mem() >= vm.getMemory();
    }
}
