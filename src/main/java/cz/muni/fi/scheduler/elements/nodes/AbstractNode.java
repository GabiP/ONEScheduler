package cz.muni.fi.scheduler.elements.nodes;

import org.opennebula.client.PoolElement;

/**
 * Abstract class used to represent more complex components of the basic sytem elements.
 * 
 * @author Andras Urge
 */
public abstract class AbstractNode {
    
    /**
     * Loads the attributes of the node from its corresponding element.
     * 
     * @param element the element that contains the node
     * @param xpathExpr the xpath expression to the node inside the element 
     */
    public abstract void load(PoolElement element, String xpathExpr);
    
}
