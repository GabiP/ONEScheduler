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
        
    private int size;

    @Override
    public void load(PoolElement vm, String xpathExpr) {
        size = Integer.parseInt(vm.xpath(xpathExpr + "/SIZE"));
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
