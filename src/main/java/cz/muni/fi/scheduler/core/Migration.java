package cz.muni.fi.scheduler.core;

import cz.muni.fi.scheduler.elementpools.IHostPool;
import cz.muni.fi.scheduler.elements.HostElement;
import cz.muni.fi.scheduler.elements.VmElement;
import java.util.List;

/**
 *
 * @author Gabriela Podolnikova
 */
public class Migration {
    
    private IHostPool hostPool;
    
    public Migration(IHostPool hostPool) {
        this.hostPool = hostPool;
    }
 
    List<HostElement> removeCurrentHost(VmElement vm, List<HostElement> hosts) {
        Integer hostId = Integer.valueOf(vm.getDeploy_id());
        HostElement host = hostPool.getHost(hostId);
        hosts.remove(host);
        return hosts;
    }   
    
}
