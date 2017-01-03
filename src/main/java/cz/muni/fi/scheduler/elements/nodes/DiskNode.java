package cz.muni.fi.scheduler.elements.nodes;

import cz.muni.fi.one.XpathLoader;
import cz.muni.fi.one.oned.TmMadConfiguration;
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
    
    private String tmMadName;
    
    private String clone;
    
    private int size;

    @Override
    public void load(PoolElement vm, String xpathExpr) {
        size = XpathLoader.getIntOrZero(vm, xpathExpr + "/SIZE");
        datastore_id = XpathLoader.getInt(vm, xpathExpr + "/DATASTORE_ID");
        tmMadName = vm.xpath(xpathExpr + "/TM_MAD");
        clone = vm.xpath(xpathExpr + "/CLONE");
    }
    
    public Boolean hasCloneTargetSelf() {
        String cloneTarget = TmMadConfiguration.getCloneTargetInfo(tmMadName);
        return "SELF".equals(cloneTarget);
    }
    
    public Boolean hasCloneTargetSystem() {
        String cloneTarget = TmMadConfiguration.getCloneTargetInfo(tmMadName);
        return "SYSTEM".equals(cloneTarget);
    }
    
    public Boolean hasLnTargetSelf() {
        String lnTarget = TmMadConfiguration.getLnTargetInfo(tmMadName);
        return "SELF".equals(lnTarget);
    }
    
    public Boolean hasLnTargetSystem() {
        String lnTarget = TmMadConfiguration.getLnTargetInfo(tmMadName);
        return "SYSTEM".equals(lnTarget);
    }
    
    public boolean isPersistent() {
        return "NO".equals(clone);
    }
    
    public boolean copyToSystem() {
        if (this.isPersistent()) {
            return hasLnTargetSystem();
        } else {
            return hasCloneTargetSystem();
        }
    }
    
    public boolean copyToImage() {
        if (this.isPersistent()) {
            return hasLnTargetSelf();
        } else {
            return hasCloneTargetSelf();
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

    public String getTmMadName() {
        return tmMadName;
    }

    public void setTmMadName(String tmMadName) {
        this.tmMadName = tmMadName;
    }

    public String getClone() {
        return clone;
    }

    public void setClone(String clone) {
        this.clone = clone;
    }

    @Override
    public String toString() {
        return "DiskNode{" + "datastore_id=" + datastore_id + ", tmMadName=" + tmMadName + ", clone=" + clone + ", size=" + size + '}';
    }
}
