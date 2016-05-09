/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.scheduler;

import cz.muni.fi.pools.VmXmlPool;
import cz.muni.fi.resources.VmXml;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Andras Urge
 */
public class MaxMinCalculator implements IUserPriorityCalculator {
    
    private VmXmlPool vmPool;

    public MaxMinCalculator(VmXmlPool vmPool) {
        this.vmPool = vmPool;
    }

    @Override
    public Map<Integer, Float> getUserPriorities(Set<Integer> userIds) {
        Map<Integer, Float> userPriorities = new HashMap<>();
        for (Integer userId : userIds) {
            List<VmXml> vms = vmPool.getAllVmsByUser(userId);
            float priority = 0;
            for (VmXml vm : vms) {
                float penalty = vm.getCpu();
                float runTime = vm.getRunTime();
                if (runTime == 0) {
                    runTime = getMaxRuntime(vms);
                }
                priority += runTime*penalty;
                System.out.println("VM Stats: id-" + vm.getVmId() + " walltime-" + runTime + " penalty-" + penalty);
            }
            userPriorities.put(userId, priority);
        }
        return userPriorities;
    }

    private int getMaxRuntime(List<VmXml> vms) {
        // TODO: get from DB
        int maxRunTime = 0;
        for (VmXml vm : vms) {
            int runTime = vm.getRunTime();
            if (runTime > maxRunTime) {
                maxRunTime = runTime;
            }
        }
        return maxRunTime;
    }
    
    
    
}
