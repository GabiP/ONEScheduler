/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.scheduler.resources.nodes;

import org.opennebula.client.PoolElement;

/**
 *
 * @author Andras Urge
 */
public class DiskNode extends AbstractNode {
    
    private Integer datastore_id;
    
    private int size;

    @Override
    public void load(PoolElement vm, String xpathExpr) {
        size = Integer.parseInt(vm.xpath(xpathExpr + "/SIZE"));
        datastore_id = Integer.parseInt(vm.xpath(xpathExpr + "/DATASTORE_ID"));
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
    
}
