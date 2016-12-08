/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.scheduler.queues;

import cz.muni.fi.extensions.MapExtension;
import cz.muni.fi.extensions.QueueListExtension;
import cz.muni.fi.extensions.VmListExtension;
import cz.muni.fi.scheduler.elementpools.IUserPool;
import cz.muni.fi.scheduler.fairshare.UserPriorityCalculator;
import cz.muni.fi.scheduler.elements.UserElement;
import cz.muni.fi.scheduler.elements.VmElement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Andras Urge
 */
public class UserGroupFairshareMapper implements QueueMapper {
    
    private UserPriorityCalculator calculator;
    private IUserPool userPool;

    public UserGroupFairshareMapper(UserPriorityCalculator calculator, IUserPool userPool) {
        this.calculator = calculator;
        this.userPool = userPool;
    }

    @Override
    public List<Queue> mapQueues(List<VmElement> vms) {
        Map<Integer, Float> userPriorities = calculator.getUserPriorities(VmListExtension.getUserIds(vms));  
        Map<Integer, Float> userGroupPriorities = getUserGroupPriorities(userPriorities);  
        List<Integer> sortedUserGroups = MapExtension.sortByValue(userGroupPriorities);
        
        List<Queue> queues = new ArrayList<>();
        for (int i=0; i<sortedUserGroups.size(); i++) {
            int userGroupId = sortedUserGroups.get(i);
            Queue queue = new Queue("UserGroup" + userGroupId, userGroupPriorities.get(userGroupId), new ArrayList<>());
            queues.add(queue);
        }
        
        List<Integer> sortedUsers = MapExtension.sortByValue(userPriorities);
        Map<Integer, List<VmElement>> userVms = VmListExtension.getUserVms(vms);
        for (int i=0; i<sortedUsers.size(); i++) {
            int bestUserGroupId = getBestUserGroupId(sortedUsers.get(i), sortedUserGroups);
            Queue queue = QueueListExtension.getQueueByName(queues, "UserGroup" + bestUserGroupId);
            addVmsToQueue(userVms.get(i), queue);
        }
        
        return queues;        
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
    
    private int getBestUserGroupId(int userGroupId, List<Integer> sortedUserGroups) {
        UserElement user = userPool.getUser(userGroupId);
        int bestUserGroup = user.getGroups().get(0);
        for (int j=1; j<user.getGroups().size(); j++) {
            int userGroup = user.getGroups().get(j);
            if (sortedUserGroups.indexOf(userGroup) < sortedUserGroups.indexOf(bestUserGroup)) {
                bestUserGroup = userGroup;
            }                
        }
        return bestUserGroup;
    }
    
    private void addVmsToQueue(List<VmElement> userVms, Queue queue) {
        Collections.sort(userVms, (VmElement vm1, VmElement vm2) -> {
            return vm1.getVmId().compareTo(vm2.getVmId());
        });
        for (int i=0; i<userVms.size(); i++) {
            queue.queue(userVms.get(i));
        }
    }   
}
