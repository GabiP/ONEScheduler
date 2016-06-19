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
public abstract class AbstractNode {
    
    public abstract void load(PoolElement element, String xpathExpr);
    
}
