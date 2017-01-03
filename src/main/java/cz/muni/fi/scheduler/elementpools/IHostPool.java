package cz.muni.fi.scheduler.elementpools;

import cz.muni.fi.scheduler.elements.HostElement;
import java.util.List;

/**
 * Host pool interface.
 * @author Gabriela Podolnikova
 */
public interface IHostPool {
    
    List<HostElement> getHosts();
    
    List<HostElement> getActiveHosts();
    
    List<Integer> getHostsIds();
    
    HostElement getHost(int id);
}
