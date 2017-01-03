package cz.muni.fi.scheduler.queues;

import cz.muni.fi.extensions.MapExtension;
import cz.muni.fi.extensions.QueueListExtension;
import cz.muni.fi.extensions.UserListExtension;
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
import java.util.Set;

/**
 * Class that maps virtual machines into queues based 
 * on the fairshare priority of their user groups.
 * 
 * @author Andras Urge
 */
public class UserGroupFairshareMapper implements IQueueMapper {
    
    private UserPriorityCalculator calculator;
    private IUserPool userPool;
    
    private Map<Integer, Float> userPercentages;
    private Map<Integer, Float> userGroupPercentages;

    public UserGroupFairshareMapper(UserPriorityCalculator calculator, IUserPool userPool, 
            Map<Integer, Float> userPercentages, Map<Integer, Float> userGroupPercentages) {
        this.calculator = calculator;
        this.userPool = userPool;
        this.userPercentages = userPercentages;
        this.userGroupPercentages = userGroupPercentages;
    }

    /**
     * Creates queues for the user groups of the provided VMs. The queues are filled
     * with the the corresponding VMs and ordered by the fairshare priorities of
     * the user groups. The VMs inside a Queue are ordered by the priority of their 
     * users.
     * 
     * @param vms the virtual machines to be mapped
     * @return list of ordered queues
     */
    @Override
    public List<Queue> mapQueues(List<VmElement> vms) {        
        Set<Integer> userIds = UserListExtension.getUserIds(userPool.getUsers());
        Map<Integer, Float> userPriorities = calculator.getUserPriorities(userIds);  
        
        Map<Integer, Float> userGroupPriorities = getUserGroupPriorities(userPriorities);  
        userGroupPriorities = applyFairsharePercentages(userGroupPriorities, userGroupPercentages);
        List<Integer> sortedUserGroups = MapExtension.sortByValue(userGroupPriorities);
        
        // create queues for the user groups
        List<Queue> queues = new ArrayList<>();
        for (int userGroupId : sortedUserGroups) {
            Queue queue = new Queue("UserGroup" + userGroupId, userGroupPriorities.get(userGroupId), new ArrayList<>());
            queues.add(queue);
        }
        
        // add vms to queues sorted by user priority
        userPriorities = applyFairsharePercentages(userPriorities, userPercentages);        
        List<Integer> sortedUsers = MapExtension.sortByValue(userPriorities);
        Map<Integer, List<VmElement>> userVms = VmListExtension.getUserVms(vms);
        for (int i=0; i<sortedUsers.size(); i++) {
            int bestUserGroupId = getBestUserGroupId(sortedUsers.get(i), sortedUserGroups);
            Queue queue = QueueListExtension.getQueueByName(queues, "UserGroup" + bestUserGroupId);
            addVmsToQueue(userVms.get(i), queue);
        }
        
        // remove queues without vms
        for (Queue q: queues) {
            if(q.isEmpty()) {
                queues.remove(q);
            }
        }
        
        return queues;        
    }
    
    /**
     * Calculates the fairshare priorities for the user groups. The fairshare 
     * priority of a user group is equal to the sum of its users' fairshare
     * priorities.
     * 
     * @param userPriorities the fairshare priorities of users 
     * @return the user group fairshare priorities
     */
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
    
    /**
     * Adjusts the provided fairshare priorities by the fairshare percentages.
     * 
     * @param priorities the fairshare priorities
     * @param percentages the fairshare percentages
     * @return the adjusted fairshare priorities
     */
    private Map<Integer, Float> applyFairsharePercentages(Map<Integer, Float> priorities, Map<Integer, Float> percentages) {
        for (int id : priorities.keySet()) {
            float modifier = percentages.getOrDefault(id, 100f);
            priorities.put(id, priorities.get(id) / modifier);
        }
        return priorities;
    }
    
    /**
     * Returns the user's user group that has the best fairshare priority.
     * 
     * @param userId the ID of the user
     * @param sortedUserGroups user groups sorted by their fairshare priority
     * @return the user's user group with the best fairshare priority
     */
    private int getBestUserGroupId(int userId, List<Integer> sortedUserGroups) {
        UserElement user = userPool.getUser(userId);
        int bestUserGroup = user.getGroups().get(0);
        for (int j=1; j<user.getGroups().size(); j++) {
            int userGroup = user.getGroups().get(j);
            if (sortedUserGroups.indexOf(userGroup) < sortedUserGroups.indexOf(bestUserGroup)) {
                bestUserGroup = userGroup;
            }                
        }
        return bestUserGroup;
    }
    
    /**
     * Inserts the vms to the queue. The vms are first ordered by their ID. 
     * 
     * @param userVms
     * @param queue 
     */
    private void addVmsToQueue(List<VmElement> userVms, Queue queue) {
        Collections.sort(userVms, (VmElement vm1, VmElement vm2) -> {
            return vm1.getVmId().compareTo(vm2.getVmId());
        });
        for (VmElement userVm : userVms) {
            queue.queue(userVm);
        }
    }   
}
