/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.resources;

import java.util.ArrayList;
import java.util.List;
import org.opennebula.client.PoolElement;

/**
 *
 * @author Andras Urge
 */
public class NodeElementLoader {
    
     public static <T extends NodeElement> List<T> getNodeElements(PoolElement element, Class<T> clazz) throws InstantiationException, IllegalAccessException {
        List<T> list = new ArrayList<>();
        int i = 1;        
        while (clazz.newInstance().exists(element, i)) {
            T xml = clazz.newInstance(); 
            xml.load(element, i);
            list.add(xml);
            i++;           
        }
        return list;
    }
    
}
