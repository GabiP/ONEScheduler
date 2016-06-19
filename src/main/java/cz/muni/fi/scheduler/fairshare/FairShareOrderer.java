/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.scheduler.fairshare;

import cz.muni.fi.scheduler.resources.VmElement;
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
    
    public List<VmElement> orderVms(List<VmElement> vms) {
        Map<Integer, Float> userPriorities = priorityCalculator.getUserPriorities(getUserIds(vms));
        Map<VmElement, Float> vmPriorities = new HashMap<>();
        for (VmElement vm : vms) {
            Float vmPriority = userPriorities.get(vm.getUid());
            vmPriorities.put(vm, vmPriority);
        }
        List<VmElement> orderedVms = sortByValue(vmPriorities);
        
        return orderedVms;
    }

    private Set<Integer> getUserIds(List<VmElement> vms) {
        Set<Integer> userIds = new HashSet<>();
        for (VmElement vm : vms) {
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
