/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.scheduler.fairshare;

import cz.muni.fi.scheduler.elementpools.IVmPool;
import cz.muni.fi.scheduler.fairshare.historyrecords.HistoryRecord;
import cz.muni.fi.scheduler.fairshare.historyrecords.HistoryRecordManager;
import cz.muni.fi.scheduler.resources.VmElement;
import cz.muni.fi.scheduler.resources.nodes.HistoryNode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This class is responsible to assign a priority to the users based on the 
 * history of their virtual machines. The lower the value, the higher priority
 * will the user get.
 * 
 * @author Andras Urge
 */
public abstract class AbstractPriorityCalculator {
        
    private IVmPool vmPool; 
    private boolean useHistoryRecords;

    public AbstractPriorityCalculator(IVmPool vmPool, boolean useHistoryRecords) {
        this.vmPool = vmPool;
        this.useHistoryRecords = useHistoryRecords;
    }
    
    /**
     * Calculates the priority for each inputted user.
     * The user priority equals to the sum of priorities 
     * calculated for each of the users virtual machines.
     * 
     * @param userIds 
     * @return The user IDs paired with their priority
     */
    public Map<Integer, Float> getUserPriorities(Set<Integer> userIds) {
        Map<Integer, Float> userPriorities = new HashMap<>();
        for (Integer userId : userIds) {
            List<VmElement> vms = vmPool.getAllVmsByUser(userId);
            float priority = 0;
            for (VmElement vm : vms) {                
                if (vm.getRunTime() == 0 && vm.getState() != 6) {
                    // assign a starting priority if the vm didnt run yet
                    priority += getMaxRuntime(vms) * getPenalty(vm);
                } 
                else {
                    priority += getPriority(vm, useHistoryRecords);     
                }
                System.out.println(userId + " - Priority: " + priority);
            }
            userPriorities.put(userId, priority);
        }
        return userPriorities;
    }        
    
    /**
     * Returns the highest runtime of the inputted virtual machines.
     * 
     * @param vms
     * @return The highest runtime
     */
    // TODO : what if all are 0?
    private int getMaxRuntime(List<VmElement> vms) {
        // TODO: maybe get from DB
        int maxRunTime = 0;
        for (VmElement vm : vms) {
            int runTime = vm.getRunTime();
            if (runTime > maxRunTime) {
                maxRunTime = runTime;
            }
        }
        return maxRunTime;
    }    
    
    /**
     * Calculates the priority of a virtual machine.
     * 
     * @param vm 
     * @return The priority of the virtual machine
     */
    private float getPriority(VmElement vm, boolean useHistoryRecord) { 
        if (useHistoryRecord) {
            return getPriorityFromHistory(vm);
        } 
        return vm.getRunTime()*getPenalty(vm);
    }     
    
    /**
     * Calculates the priority of a virtual machine based on its historical
     * parameters.
     * 
     * @param vm 
     * @return The priority of the virtual machine
     */
    private float getPriorityFromHistory(VmElement vm) { 
        float priority = 0;  
        for (HistoryNode history : vm.getHistories()) {
            HistoryRecord record = HistoryRecordManager.loadHistoryRecord(vm, history);
            VmElement vmFromHistory = HistoryRecordManager.createVmFromHistory(vm, record); 
            
            int historyRunTime = vm.getHistoryRuntime(history);            
            priority += historyRunTime * getPenalty(vmFromHistory);        
        }
        return priority;
    }     
    
    /**
     * Returns a penalty calculated for the given virtual machine. The 
     * implementation  of this method is defining the strategy of the 
     * fair-share ordering.
     * 
     * @param vm
     * @return Penalty for the virtual machine
     */
    protected abstract float getPenalty(VmElement vm);    
}
