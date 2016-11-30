package cz.muni.fi.one.pools;

import cz.muni.fi.one.mappers.ClusterMapper;
import cz.muni.fi.scheduler.elementpools.IClusterPool;
import cz.muni.fi.scheduler.resources.ClusterElement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.opennebula.client.Client;
import org.opennebula.client.OneResponse;
import org.opennebula.client.cluster.Cluster;
import org.opennebula.client.cluster.ClusterPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class represents OpenNebula's ClusterPool containing all instances of clusters in the system.
 * The pool is accessed through OpenNebula's Client. The Client represents the connection with the core of OpenNebula.
 * Each OpenNebula's instance of Cluster is mapped to our ClusterElement.
 * 
 * @author Gabriela Podolnikova
 */
public class ClusterElementPool implements IClusterPool {
    
    protected final Logger log = LoggerFactory.getLogger(getClass());
    
    private final ClusterPool cp;
    
    public ClusterElementPool(Client oneClient) {
        cp = new ClusterPool(oneClient);
    }

    /**
     * Goes through the pool and maps all clusters.
     * @return the list of mapped clusters
     */
    @Override
    public List<ClusterElement> getClusters() {
        List<ClusterElement> clusters = new ArrayList<>();
        OneResponse cpr = cp.info();
        if (cpr.isError()) {
            log.error(cpr.getErrorMessage());
        }
        Iterator<Cluster> itr = cp.iterator();
        while (itr.hasNext()) {
            Cluster element = itr.next();
            System.out.println("Cluster: " + element);
            ClusterElement c = ClusterMapper.map(element);
            System.out.println("Cluster: " + c);
            clusters.add(c);
        }
        return clusters;
    }

    @Override
    public ClusterElement getCluster(int id) {
        cp.info();
        return ClusterMapper.map(cp.getById(id));
    }
}
