/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.scheduler.fairshare;

import cz.muni.fi.extensions.VmListExtension;
import cz.muni.fi.scheduler.elementpools.IVmPool;
import cz.muni.fi.scheduler.fairshare.penaltycalculators.IVmPenaltyCalculator;
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
public class UserPriorityCalculator {
        
    private IVmPool vmPool; 
    private IVmPenaltyCalculator penaltyCalculator;
    private IUserFairshareRecordManager userRecordManager;
    private IVmFairshareRecordManager vmRecordManager;

    public UserPriorityCalculator(IVmPool vmPool, IVmPenaltyCalculator penaltyCalculator, IUserFairshareRecordManager userRecordManager, IVmFairshareRecordManager vmRecordManager) {
        this.vmPool = vmPool;
        this.penaltyCalculator = penaltyCalculator;
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
            float currentUserPriority = calculateCurrentUserPriority(userId);
            float pastUserPriority = calculatePastUserPriority(userId);
            userRecordManager.storePriority(userId, pastUserPriority);
            
            userPriorities.put(userId, pastUserPriority + currentUserPriority);
        }
        return userPriorities;
    }  
    
    private float calculateCurrentUserPriority(Integer userId) {
        List<VmElement> vms = vmPool.getVmsByUser(userId);
        
        float currentPriority = 0;
        for (VmElement vm : vms) {                
            if (vm.getRunTime() == 0) {
                // assign a starting priority if the vm didnt run yet                
                currentPriority += getStartingVmPriority(vm);
            } 
            else {
                currentPriority += getVmPriority(vm);     
            }
        }
        return currentPriority;
    } 
    
    private float calculatePastUserPriority(Integer userId) {
        float pastPriority = userRecordManager.getPriority(userId);
        List<VmElement> vms;
        if (pastPriority == 0) { 
            vms = vmPool.getVms(userId, 6);            
        } else {
            vms = getNewlyDoneVms(userId);              
        }  
        for (VmElement vm : vms) {
            pastPriority += getVmPriority(vm);
        }
        vmRecordManager.delete(VmListExtension.getVmIds(vms));
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
    
    private float getStartingVmPriority(VmElement vm) {   
        List<VmElement> allVms = vmPool.getAllVmsByUser(vm.getUid());
        float maxVmRuntime = Collections.max(VmListExtension.getRuntimes(allVms));
        float startingPriority =  maxVmRuntime * penaltyCalculator.getPenalty(vm);
        
        VmFairshareRecord newRecord = vmRecordManager.createRecord(vm, 0);        
        vmRecordManager.storeRecord(newRecord);
            
        return startingPriority;
    }  
    
    /**
     * Calculates the priority of a virtual machine.
     * 
     * @param vm 
     * @return The priority of the virtual machine
     */
    private float getVmPriority(VmElement vm) {         
        float activeVmPriority = calculateActiveVmPriority(vm);
        float pastVmPriority = calculatePastVmPriority(vm);
        
        if (vm.getState() != 6) {
            VmFairshareRecord newRecord = vmRecordManager.createRecord(vm, pastVmPriority);
            vmRecordManager.storeRecord(newRecord);
        }
        return activeVmPriority + pastVmPriority;
    }  
    
    private float calculateActiveVmPriority(VmElement vm) {
        float priority = 0;
        if (vm.getState() == 3) {
            HistoryNode lastHistory = vm.getHistories().get(vm.getHistories().size()-1);
            int historyRunTime = vm.getHistoryRuntime(lastHistory);            
            priority += historyRunTime * penaltyCalculator.getPenalty(vm);
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
                priority += historyRunTime * penaltyCalculator.getPenalty(vmFromRecord);
            }       
        }
        return priority;
    }   
}
