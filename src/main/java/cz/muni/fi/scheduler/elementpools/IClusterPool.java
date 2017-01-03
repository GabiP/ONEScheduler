package cz.muni.fi.scheduler.elementpools;

import cz.muni.fi.scheduler.elements.ClusterElement;
import java.util.List;

/**
 * A pool interface.
 * @author Gabriela Podolnikova
 */
public interface IClusterPool {
    
    List<ClusterElement> getClusters();
    
    ClusterElement getCluster(int id);
}
