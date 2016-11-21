/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.scheduler.fairshare;

import cz.muni.fi.extensions.VmListExtension;
import cz.muni.fi.scheduler.resources.VmElement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * This class is responsible for ordering the given Virtual Machines. 
 * The strategy of the ordering depends on the chosen AbstractPriorityCalculator.
 * 
 * @author Andras Urge
 */
public class UserFairShareOrderer implements IFairShareOrderer {
    
    private UserPriorityCalculator calculator;

    public UserFairShareOrderer(UserPriorityCalculator priorityCalculator) {
        this.calculator = priorityCalculator;
    }
    
    /**
     * Orders the inputted Virtual Machines based on the chosen 
     * AbstractPriorityCalculator. 
     * 
     * @param vms The Virtual Machines to order
     * @return The ordered Virtual Machines
     */
    @Override
    public List<VmElement> orderVms(List<VmElement> vms) {
        Map<Integer, Float> userPriorities = calculator.getUserPriorities(VmListExtension.getUserIds(vms));
        Map<VmElement, Float> vmPriorities = new HashMap<>();
        for (VmElement vm : vms) {
            Float vmPriority = userPriorities.get(vm.getUid());
            vmPriorities.put(vm, vmPriority);
        }
        List<VmElement> orderedVms = orderByPriority(vmPriorities);
        
        return orderedVms;
    }

    /**
     * Orders the the virtual machines by their priority. In case some virtual
     * machines have the same priority the ones which were created sooner will 
     * go first.
     * 
     * @param map The virtual machine - priority pairs  
     * @return Sorted virtual machines
     */
    private static List<VmElement> orderByPriority(Map<VmElement, Float> map) {
        // Create Comparator
        Comparator<Map.Entry<VmElement, Float>> vmPriorityComparator = (Map.Entry<VmElement, Float> o1, Map.Entry<VmElement, Float> o2) -> {
            int priorityCompare = o1.getValue().compareTo(o2.getValue());
            if (priorityCompare != 0) {
                return priorityCompare;
            } else {
                int idCompare = o1.getKey().getVmId().compareTo(o2.getKey().getVmId());
                return idCompare;
            }           
        };
        
        // Create List from Map
        List list = new LinkedList(map.entrySet());
       
        // Sort List by the Comparator
        Collections.sort(list, vmPriorityComparator);

        // Return the ordered keys from the List
        List orderedKeys = new ArrayList();
        for (Iterator it = list.iterator(); it.hasNext();) {
            orderedKeys.add(((Map.Entry) it.next()).getKey());
        } 
        return orderedKeys;
    }    
}
