/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.extensions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Class containing helper methods for a Map.
 * 
 * @author Andras Urge
 */
public class MapExtension {
    
    /**
     * Returns the keys sorted according to the values of the map.
     * 
     * @param unsortedMap the map
     * @return sorted keys
     */
    public static List sortByValue(Map unsortedMap) {
        // Create value comparator
        Comparator<Map.Entry> valueComparator = (Map.Entry e1, Map.Entry e2) -> {
            Comparable value1 = (Comparable) e1.getValue();
            Comparable value2 = (Comparable) e2.getValue();
            return value1.compareTo(value2);                  
        };
        
        // Create List from Map
        List list = new LinkedList(unsortedMap.entrySet());
       
        // Sort List by the Comparator
        Collections.sort(list, valueComparator);

        // Return the ordered keys from the List        
        List orderedKeys = new ArrayList<>();
        for (Object aList : list) {
            Map.Entry entry = (Map.Entry) aList;
            orderedKeys.add(entry.getKey());
        }
        
        return orderedKeys;
    }
}
