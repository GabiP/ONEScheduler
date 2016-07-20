package cz.muni.fi.scheduler.resources.nodes;

import org.opennebula.client.PoolElement;

/**
 * This class represents a DiskNode.
 * A virtual machine can have multiple disk nodes.
 * Loads the data from xml by using OpenNebula's API.
 * 
 * @author Andras Urge
 */
public class DiskNode extends AbstractNode {
    
    private Integer datastore_id;
    
    private int size;

    @Override
    public void load(PoolElement vm, String xpathExpr) {
        size = Integer.parseInt(vm.xpath(xpathExpr + "/SIZE"));
        try {
            datastore_id = Integer.parseInt(vm.xpath(xpathExpr + "/DATASTORE_ID"));
        } catch (Exception e) {
            datastore_id = null;
        }
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public Integer getDatastore_id() {
        return datastore_id;
    }

    public void setDatastore_id(Integer datastore_id) {
        this.datastore_id = datastore_id;
    }

    @Override
    public String toString() {
        return "DiskNode{" + "datastore_id=" + datastore_id + ", size=" + size + '}';
    }  
}
