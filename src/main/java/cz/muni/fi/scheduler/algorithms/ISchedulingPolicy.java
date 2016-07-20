package cz.muni.fi.scheduler.algorithms;

import cz.muni.fi.scheduler.resources.HostElement;
import cz.muni.fi.scheduler.resources.VmElement;
import java.util.List;

/**
 * This class represents an interface that all scheduling policices should be impelementing.
 * 
 * @author Gabriela Podolnikova
 */
public interface ISchedulingPolicy {
    
    public HostElement selectHost(List<HostElement> hosts, VmElement vm);
}
