package cz.muni.fi.one.mappers;

import cz.muni.fi.one.XpathLoader;
import cz.muni.fi.scheduler.elements.ClusterElement;
import org.opennebula.client.cluster.Cluster;

/**
 * This class maps OpenNebula's Cluster class to ClusterElement class.
 * Retreives from OpenNebula's Cluster instance its attributes by using OpenNebula's Java API.
 * For further information of Java API please refer to: https://docs.opennebula.org/5.2/integration/system_interfaces/java.html
 * 
 * @author Andras Urge
 */
public class ClusterMapper {
    
    /**
     * Maps Cluster to ClusterElement.
     * @param cluster the cluster to be mapped
     * @return 
     */
    public static ClusterElement map(Cluster cluster) {
        ClusterElement result = new ClusterElement();
        cluster.info();        
        
        result.setId(XpathLoader.getInt(cluster, "/CLUSTER/ID"));         
        result.setName(cluster.xpath("/CLUSTER/NAME"));
        result.setHosts(XpathLoader.getIntList(cluster, "/CLUSTER/HOSTS/ID"));
        result.setDatastores(XpathLoader.getIntList(cluster, "/CLUSTER/DATASTORES/ID"));
        result.setReservedCpu(XpathLoader.getFloatOrZero(cluster, "/CLUSTER/TEMPLATE/RESERVED_CPU")/100);
        result.setReservedMemory(XpathLoader.getIntOrZero(cluster, "/CLUSTER/TEMPLATE/RESERVED_MEM")/1024);
        
        return result;
    }
    
    
    
}
