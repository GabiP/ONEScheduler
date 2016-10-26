package cz.muni.fi.scheduler.filters.hosts.strategies;

import cz.muni.fi.scheduler.resources.HostElement;
import cz.muni.fi.scheduler.resources.VmElement;

/**
 *
 * @author Andras Urge
 */
public class FilterHostByMaxCpu implements IHostFilterStrategy {
    
    /**
     * Tests whether the maximum CPU of a host is sufficient for the VM.
     * 
     * @param vm Virtual Machine to be checked
     * @param host Host to be checked
     * @return true if the the maximum CPU of a host is sufficient for the vm
     */
    @Override
    public boolean test(VmElement vm, HostElement host) {
        return host.getMax_cpu() >= vm.getCpu();
    }
    
}
