/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.scheduler.fairshare.historyrecords;

import cz.muni.fi.scheduler.resources.VmElement;
import java.util.List;

/**
 *
 * @author Andras Urge
 */
public interface IVmFairshareRecordManager {
    
    public VmFairshareRecord getRecord(int vmId);
    
    public List<VmFairshareRecord> getRecords(int userId);
    
    public void storeRecord(VmFairshareRecord record);
        
    public void delete(int vmId);
    
    public void delete(List<Integer> vmIds);
    
    /**
     * Creates a new VmElement by updating the data in the inputted 
     * VmElement from the given History Record.
     * 
     * @param vm The VmElement we want to update
     * @param record The history record used to update the data in vm
     * @return The updated VmElement
     */
    public VmElement createVmFromRecord(VmElement vm, VmFairshareRecord record);

}
