package cz.muni.fi.one;

import cz.muni.fi.scheduler.resources.nodes.AbstractNode;
import java.util.ArrayList;
import java.util.List;
import org.opennebula.client.PoolElement;

/**
 * An auxiliary method that is used for retreiving data from xml representation of OpenNebula's objects.
 * 
 * @author Andras Urge
 */
public class XpathLoader {
          
    public static Integer getInt(PoolElement element, String xpathExpr) {
        if (exists(element, xpathExpr)) {
            return Integer.parseInt(element.xpath(xpathExpr));            
        }
        return null;
    }  
    
    public static Float getFloat(PoolElement element, String xpathExpr) {
        if (exists(element, xpathExpr)) {
            return Float.parseFloat(element.xpath(xpathExpr));          
        }
        return null;
    } 
    
    public static List<Integer> getIntList(PoolElement element, String xpathExpr) {
        List<Integer> list = new ArrayList<>();
        int i = 1;        
        while (exists(element, xpathExpr +"["+i+"]")) {
            Integer id = Integer.parseInt(element.xpath(xpathExpr +"["+i+"]"));
            list.add(id);
            i++;           
        }
        return list;
    }
    
    /**
     * Retrieves a list of nodes (e.g.: DiskNode, HistoryNode).
     * 
     * @param <T> a type of class that we want to create = the node.
     * @param element the element from which we are retreiving the node.
     * @param clazz a type of class that we want to create = the node.
     * @param xpathExpr a string defining the xpath expression
     * @return the list of desired nodes
     * @throws InstantiationException
     * @throws IllegalAccessException 
     */
    public static <T extends AbstractNode> List<T> getNodeList(PoolElement element, Class<T> clazz, String xpathExpr) throws InstantiationException, IllegalAccessException {
        List<T> list = new ArrayList<>();
        int i = 1;        
        while (exists(element, xpathExpr +"["+i+"]")) {
            T node = clazz.newInstance(); 
            node.load(element, xpathExpr +"["+i+"]");
            list.add(node);
            i++;           
        }
        return list;
    }
     
    private static boolean exists(PoolElement element, String xpathExpr) {
        String node = element.xpath(xpathExpr);
        return !node.equals("");
    }
}
