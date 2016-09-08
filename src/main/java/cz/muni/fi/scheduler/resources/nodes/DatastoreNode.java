package cz.muni.fi.scheduler.resources.nodes;

import org.opennebula.client.PoolElement;

/**
 * This class represents a Datastore node.
 * A host can have multiple datastore nodes.
 * Loads the data from xml by using OpenNebula's API.
 * 
 * @author Gabriela Podolnikova
 */
public class DatastoreNode extends AbstractNode {
        
    private Integer id_ds;
    private Integer free_mb;
    private Integer total_mb;
    private Integer used_mb;

    @Override
    public void load(PoolElement host, String xpathExpr) {
         id_ds = Integer.parseInt(host.xpath(xpathExpr + "/ID"));
         free_mb = Integer.parseInt(host.xpath(xpathExpr + "/FREE_MB"));
         total_mb = Integer.parseInt(host.xpath(xpathExpr + "/TOTAL_MB"));
         used_mb = Integer.parseInt(host.xpath(xpathExpr + "/USED_MB"));
    }
    
    public void update(Integer vmUsage) {
        free_mb -= vmUsage;
        used_mb += vmUsage;
    }
    
    /**
     * @return the id_ds
     */
    public Integer getId_ds() {
        return id_ds;
    }

    /**
     * @param id_ds the id_ds to set
     */
    public void setId_ds(Integer id_ds) {
        this.id_ds = id_ds;
    }

    /**
     * @return the free_mb
     */
    public Integer getFree_mb() {
        return free_mb;
    }

    /**
     * @param free_mb the free_mb to set
     */
    public void setFree_mb(Integer free_mb) {
        this.free_mb = free_mb;
    }

    /**
     * @return the total_mb
     */
    public Integer getTotal_mb() {
        return total_mb;
    }

    /**
     * @param total_mb the total_mb to set
     */
    public void setTotal_mb(Integer total_mb) {
        this.total_mb = total_mb;
    }

    /**
     * @return the used_mb
     */
    public Integer getUsed_mb() {
        return used_mb;
    }

    /**
     * @param used_mb the used_mb to set
     */
    public void setUsed_mb(Integer used_mb) {
        this.used_mb = used_mb;
    }

    @Override
    public String toString() {
        return "DatastoreNode{" + "id_ds=" + id_ds + ", free_mb=" + free_mb + ", total_mb=" + total_mb + ", used_mb=" + used_mb + '}';
    }
    
}
