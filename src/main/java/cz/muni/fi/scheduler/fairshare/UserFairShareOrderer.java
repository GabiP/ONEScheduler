/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.scheduler.fairshare;

import cz.muni.fi.extensions.VmListExtension;
import cz.muni.fi.scheduler.resources.VmElement;
import java.util.Collections;
import java.util.Comparator;
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
     * UserPriorityCalculator. 
     * 
     * @param vms The Virtual Machines to order
     * @return The ordered Virtual Machines
     */
    @Override
    public List<VmElement> orderVms(List<VmElement> vms) {
        Map<Integer, Float> userPriorities = calculator.getUserPriorities(VmListExtension.getUserIds(vms));
        List<VmElement> orderedVms = orderByUserPriority(vms, userPriorities);
        
        return orderedVms;
    }

    private List<VmElement> orderByUserPriority(List<VmElement> vms, Map<Integer, Float> userPriorities) {
        Comparator<VmElement> vmPriorityComparator = (VmElement vm1, VmElement vm2) -> {            
            Float vm1UserPriority = userPriorities.get(vm1.getUid());
            Float vm2UserPriority = userPriorities.get(vm2.getUid());
            int userPriorityCompare = vm1UserPriority.compareTo(vm2UserPriority);
            if (userPriorityCompare != 0) {
                return userPriorityCompare;
            } else {
                int idCompare = vm1.getVmId().compareTo(vm2.getVmId());
                return idCompare;
            }                     
        };
        
        Collections.sort(vms, vmPriorityComparator);
        return vms;        
    }   
}
