/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.scheduler.fairshare;

import cz.muni.fi.extensions.VmListExtension;
import cz.muni.fi.scheduler.elementpools.IVmPool;
import cz.muni.fi.scheduler.fairshare.historyrecords.IUserFairshareRecordManager;
import cz.muni.fi.scheduler.fairshare.historyrecords.IVmFairshareRecordManager;
import cz.muni.fi.scheduler.fairshare.historyrecords.VmFairshareRecord;
import cz.muni.fi.scheduler.resources.VmElement;
import cz.muni.fi.scheduler.resources.nodes.HistoryNode;
import java.util.ArrayList;
import java.util.Collections;
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
    private IUserFairshareRecordManager userRecordManager;
    private IVmFairshareRecordManager vmRecordManager;

    public AbstractPriorityCalculator(IVmPool vmPool, IUserFairshareRecordManager userRecordManager, IVmFairshareRecordManager vmRecordManager) {
        this.vmPool = vmPool;
        this.userRecordManager = userRecordManager;
        this.vmRecordManager = vmRecordManager;
    }
    
    /**
     * Calculates the priority for each inputted user.
     * 
     * @param userIds 
     * @return The user IDs paired with their priority
     */
    public Map<Integer, Float> getUserPriorities(Set<Integer> userIds) {
        Map<Integer, Float> userPriorities = new HashMap<>();
        for (Integer userId : userIds) {                                    
            float activePriority = calculateActiveUserPriority(userId);
            float pastPriority = calculatePastUserPriority(userId);
            userRecordManager.storePriority(userId, pastPriority);
            
            userPriorities.put(userId, pastPriority + activePriority);
        }
        return userPriorities;
    }  
    
    private float calculateActiveUserPriority(Integer userId) {
        List<VmElement> vms = vmPool.getVmsByUser(userId);
        
        float activePriority = 0;
        for (VmElement vm : vms) {                
            if (vm.getRunTime() == 0 && vm.getState() != 6) {
                // assign a starting priority if the vm didnt run yet
                float maxVmRuntime = Collections.max(VmListExtension.getRuntimes(vms));
                activePriority +=  maxVmRuntime * getPenalty(vm);
            } 
            else {
                activePriority += getPriority(vm, true);     
            }
        }
        return activePriority;
    } 
    
    private float calculatePastUserPriority(Integer userId) {
        float pastPriority = userRecordManager.getPriority(userId);
        if (pastPriority == 0) {                
            pastPriority += getPriority(vmPool.getVms(userId, 6), false);
        } else {
            pastPriority += getPriority(getNewlyDoneVms(userId), true);                
        }  
        return pastPriority;
    }
    
    private List<VmElement> getNewlyDoneVms(int userId) {
        List<VmFairshareRecord> lastVmRecords = vmRecordManager.getRecords(userId); 
        List<Integer> currentVmIds = VmListExtension.getVmIds(vmPool.getVmsByUser(userId));
        
        List<VmElement> newlyDoneVms = new ArrayList<>();
        for (VmFairshareRecord record : lastVmRecords) {
            if (!currentVmIds.contains(record.getVmId())) {
                VmElement newlyDoneVm = vmPool.getVm(record.getVmId());
                newlyDoneVms.add(newlyDoneVm);
            }
        }
        return newlyDoneVms;
    }
    
    private float getPriority(List<VmElement> vms, boolean useFairshareRecord) {
        float priority = 0;        
        for (VmElement vm : vms) {
            priority += getPriority(vm, useFairshareRecord);
        }
        return priority;
    }
    
    /**
     * Calculates the priority of a virtual machine.
     * 
     * @param vm 
     * @return The priority of the virtual machine
     */
    private float getPriority(VmElement vm, boolean useFairshareRecord) { 
        if (useFairshareRecord) {
            float activePriority = calculateActiveVmPriority(vm);
            float pastPriority = calculatePastVmPriority(vm);

            VmFairshareRecord newRecord = new VmFairshareRecord(
                    vm.getVmId(), vm.getUid(), pastPriority, vm.getLastClosedHistory().getSequence(), vm.getCpu(), vm.getMemory());
            vmRecordManager.storeRecord(newRecord);

            return activePriority + pastPriority;
        } else {
            return vm.getRunTime()*getPenalty(vm);
        }
    }  
    
    private float calculateActiveVmPriority(VmElement vm) {
        float priority = 0;
        if (vm.getState() == 3) {
            HistoryNode lastHistory = vm.getHistories().get(vm.getHistories().size()-1);
            int historyRunTime = vm.getHistoryRuntime(lastHistory);            
            priority += historyRunTime * getPenalty(vm);
        }
        return priority;
    }    
    
    private float calculatePastVmPriority(VmElement vm) {
        VmFairshareRecord vmRecord = vmRecordManager.getRecord(vm.getVmId());
        if (vmRecord == null) {
            vmRecord = new VmFairshareRecord(
                    vm.getVmId(), vm.getUid(), 0, -1, vm.getCpu(), vm.getMemory());
        }
        float priority = vmRecord.getPriority();        
        
        VmElement vmFromRecord = vmRecordManager.createVmFromRecord(vm, vmRecord); 
        for (HistoryNode history : vm.getClosedHistories()) {
            if (history.getSequence() > vmRecord.getLastClosedHistory()) {                
                int historyRunTime = vm.getHistoryRuntime(history);            
                priority += historyRunTime * getPenalty(vmFromRecord);
            }       
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
