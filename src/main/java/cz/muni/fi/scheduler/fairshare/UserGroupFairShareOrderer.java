/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.scheduler.fairshare;

import cz.muni.fi.extensions.UserListExtension;
import cz.muni.fi.scheduler.elementpools.IUserPool;
import cz.muni.fi.scheduler.resources.UserElement;
import cz.muni.fi.scheduler.resources.VmElement;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This class is responsible for ordering the given Virtual Machines. 
 * The strategy of the ordering depends on the chosen AbstractPriorityCalculator.
 * 
 * @author Andras Urge
 */
public class UserGroupFairShareOrderer implements IFairShareOrderer {
    
    private UserPriorityCalculator calculator;
    private IUserPool userPool;

    public UserGroupFairShareOrderer(UserPriorityCalculator priorityCalculator, IUserPool userPool) {
        this.calculator = priorityCalculator;
        this.userPool = userPool;
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
        Set<Integer> userIds = UserListExtension.getUserIds(userPool.getUsers());
        Map<Integer, Float> userPriorities = calculator.getUserPriorities(userIds);  
        Map<Integer, Float> userGroupPriorities = getUserGroupPriorities(userPriorities);        
        
        // key: vm, value: smallest usergroup priority of vm
        Map<VmElement, Float> vmGroupPriorities = getVmGroupPriorities(vms, userGroupPriorities);
        
        List<VmElement> orderedVms = orderByGroupPriority(vms, vmGroupPriorities, userPriorities);
        
        return orderedVms;
    }
    
    private Map<Integer, Float> getUserGroupPriorities(Map<Integer, Float> userPriorities) {
        Map<Integer, Float> groupPriorities = new HashMap<>();
        for (int u : userPriorities.keySet()) {
            UserElement user = userPool.getUser(u);
            for (int userGroup : user.getGroups()) {
                if (groupPriorities.containsKey(userGroup)) {
                    groupPriorities.put(userGroup, userPriorities.get(userGroup) + userPriorities.get(u));
                } else {
                    groupPriorities.put(userGroup, userPriorities.get(u));
                }
            }
        }
        return groupPriorities;
    }
    
    private Map<VmElement, Float> getVmGroupPriorities(List<VmElement> vms, Map<Integer, Float> userGroupPriorities) {
        Map<VmElement, Float> result = new HashMap<>();                
        for (VmElement vm : vms) {
            UserElement user = userPool.getUser(vm.getUid());
            float minGroupPriority = userGroupPriorities.get(user.getGroups().get(0)); 
            for (int i=1; i<user.getGroups().size(); i++) {
                int groupId = user.getGroups().get(i);
                float groupPriority = userGroupPriorities.get(groupId);
                if (groupPriority < minGroupPriority) {
                    minGroupPriority = groupPriority;
                }
            }
            result.put(vm, minGroupPriority);
        }
        return result;
    }

    /**
     * ...
     * 
     * @param map The virtual machine - priority pairs  
     * @return Sorted virtual machines
     */
    private List<VmElement> orderByGroupPriority(List<VmElement> vms, Map<VmElement, Float> groupPriorities, Map<Integer, Float> userPriorities) {
        Comparator<VmElement> vmPriorityComparator = (VmElement vm1, VmElement vm2) -> {
            Float vm1GroupPriority = groupPriorities.get(vm1);
            Float vm2GroupPriority = groupPriorities.get(vm2);
            int groupPriorityCompare = vm1GroupPriority.compareTo(vm2GroupPriority);
            if (groupPriorityCompare != 0) {
                return groupPriorityCompare;
            } else {
                Float vm1UserPriority = userPriorities.get(vm1.getUid());
                Float vm2UserPriority = userPriorities.get(vm2.getUid());
                int userPriorityCompare = vm1UserPriority.compareTo(vm2UserPriority);
                if (userPriorityCompare != 0) {
                    return userPriorityCompare;
                } else {
                    int idCompare = vm1.getVmId().compareTo(vm2.getVmId());
                    return idCompare;
                }
            }           
        };
        
        Collections.sort(vms, vmPriorityComparator);
        return vms;        
    }    
}
