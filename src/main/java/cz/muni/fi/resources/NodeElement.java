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
public abstract class NodeElement {
    
    abstract void load(PoolElement element, int index); 
    
    abstract String getXpathExpr();
    
    boolean exists(PoolElement element, int index) {
        String node = element.xpath(getXpathExpr()+"["+index+"]");
        return !node.equals("");
    }
    
}
