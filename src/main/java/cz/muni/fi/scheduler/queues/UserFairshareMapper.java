/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.scheduler.queues;

import cz.muni.fi.extensions.MapExtension;
import cz.muni.fi.extensions.VmListExtension;
import cz.muni.fi.scheduler.fairshare.UserPriorityCalculator;
import cz.muni.fi.scheduler.elements.VmElement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Andras Urge
 */
public class UserFairshareMapper implements QueueMapper {
    
    private UserPriorityCalculator calculator;
    
    public UserFairshareMapper(UserPriorityCalculator calculator) {
        this.calculator = calculator;
    }

    @Override
    public List<Queue> mapQueues(List<VmElement> vms) {
        Map<Integer, Float> userPriorities = calculator.getUserPriorities(VmListExtension.getUserIds(vms));
        List<Integer> sortedUsers = MapExtension.sortByValue(userPriorities);
        Map<Integer, List<VmElement>> userVms = VmListExtension.getUserVms(vms);
        
        List<Queue> queues = new ArrayList<>();
        for (Integer sortedUser : sortedUsers) {
            int userId = sortedUser;
            Queue queue = createUserQueue(userId, userPriorities.get(userId), userVms.get(userId));
            queues.add(queue);
        }
        return queues;
    }
    
    private Queue createUserQueue(int userId, float priority, List<VmElement> userVms) {
        Collections.sort(userVms, (VmElement vm1, VmElement vm2) -> {
            return vm1.getVmId().compareTo(vm2.getVmId());
        });
        return new Queue("User" + userId, priority, userVms);
    }
}
