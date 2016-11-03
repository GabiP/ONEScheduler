package cz.muni.fi.result;

import cz.muni.fi.scheduler.resources.HostElement;
import java.util.List;

/**
 *
 * @author Gabriela Podolnikova
 */
public interface IResultManager {
    
     public boolean writeResults(List<HostElement> hostPool);
}
