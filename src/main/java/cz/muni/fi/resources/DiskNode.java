/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.resources;

import org.opennebula.client.PoolElement;

/**
 *
 * @author Andras Urge
 */
class DiskNode extends NodeElement {
    
    private static final String XPATH_EXPR = "/VM/TEMPLATE/DISK";
    
    private int size;

    @Override
    public void load(PoolElement vm, int index) {
        size = Integer.parseInt(vm.xpath(XPATH_EXPR+"["+index+"]" + "/SIZE"));
    }

    @Override
    public String getXpathExpr() {
        return XPATH_EXPR;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
    
    
}
