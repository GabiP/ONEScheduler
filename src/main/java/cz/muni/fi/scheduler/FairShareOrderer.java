/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.scheduler;

import cz.muni.fi.resources.VmXml;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Andras Urge
 */
public class FairShareOrderer {
    
    private IUserPriorityCalculator priorityCalculator;

    public FairShareOrderer(IUserPriorityCalculator priorityCalculator) {
        this.priorityCalculator = priorityCalculator;
    }
    
    public List<VmXml> orderVms(List<VmXml> vms) {
        Map<Integer, Float> userPriorities = priorityCalculator.getUserPriorities(getUserIds(vms));
        Map<VmXml, Float> vmPriorities = new HashMap<>();
        for (VmXml vm : vms) {
            Float vmPriority = userPriorities.get(vm.getUid());
            vmPriorities.put(vm, vmPriority);
        }
        List<VmXml> orderedVms = sortByValue(vmPriorities);
        
        return orderedVms;
    }

    private Set<Integer> getUserIds(List<VmXml> vms) {
        Set<Integer> userIds = new HashSet<>();
        for (VmXml vm : vms) {
            userIds.add(vm.getUid());
        }
        return userIds;
    }

    private static List sortByValue(Map map) { 
        // Create List from Map
        List list = new LinkedList(map.entrySet());
       
        // Sort List by the values
        Collections.sort(list, (Map.Entry o1, Map.Entry o2) -> ((Comparable) o1.getValue())
               .compareTo(o2.getValue()));

        // Return the ordered keys
        List orderedKeys = new ArrayList();
        for (Iterator it = list.iterator(); it.hasNext();) {
            orderedKeys.add(((Map.Entry) it.next()).getKey());
        } 
        return orderedKeys;
    }
    
}
