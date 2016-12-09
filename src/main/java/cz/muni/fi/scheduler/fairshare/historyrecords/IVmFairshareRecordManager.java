/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.scheduler.fairshare.historyrecords;

import cz.muni.fi.scheduler.elements.VmElement;
import java.util.List;

/**
 *
 * @author Andras Urge
 */
public interface IVmFairshareRecordManager {
    
    VmFairshareRecord getRecord(int vmId);
    
    List<VmFairshareRecord> getRecords(int userId);
    
    void storeRecord(VmFairshareRecord record);
        
    void delete(int vmId);
    
    void delete(List<Integer> vmIds);
    
    VmFairshareRecord createRecord(VmElement vm, float priority);
    
    /**
     * Creates a new VmElement by updating the data in the inputted 
     * VmElement from the given History Record.
     * 
     * @param vm The VmElement we want to update
     * @param record The history record used to update the data in vm
     * @return The updated VmElement
     */
    VmElement createVmFromRecord(VmElement vm, VmFairshareRecord record);
    
    void clearContent();
}
