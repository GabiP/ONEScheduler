package cz.muni.fi.scheduler.policies.hosts;

import cz.muni.fi.scheduler.core.SchedulerData;
import cz.muni.fi.scheduler.resources.HostElement;
import cz.muni.fi.scheduler.resources.VmElement;
import java.util.List;

/**
 * This class represents an interface that all placement policies should be impelementing.
 * 
 * @author Gabriela Podolnikova
 */
public interface IPlacementPolicy {
    
    public List<HostElement> sortHosts(List<HostElement> hosts, SchedulerData schedulerData);
}
