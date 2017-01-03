/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.scheduler.fairshare.historyrecords;

import cz.muni.fi.scheduler.elements.VmElement;
import java.util.List;

/**
 * The class responsible for storing fairshare records of virtual machines that
 * are not finished yet.
 * 
 * @author Andras Urge
 */
public interface IVmFairshareRecordManager {
    
    /** 
     * Returns the record for the given VM.
     * 
     * @param vmId the ID of the vm
     * @return record for the vm
     */
    VmFairshareRecord getRecord(int vmId);
    
    /** 
     * Returns the records for the VMs of the given user.
     * 
     * @param userId the ID of the user
     * @return records for the VMs of the user
     */
    List<VmFairshareRecord> getRecords(int userId);
    
    /**
     * Stores the given record.
     * 
     * @param record the record to be stored
     */
    void storeRecord(VmFairshareRecord record);
        
    /**
     * Deletes the given record.
     * 
     * @param vmId ID of the record to be deleted
     */
    void delete(int vmId);
    
    /**
     * Deletes the given records.
     * 
     * @param vmIds list of the IDs of the records to be deleted
     */
    void delete(List<Integer> vmIds);
    
    /**
     * Creates a record from the given VM with the specified fairshare usage.
     * 
     * @param vm the virtual machine
     * @param usage the fairshare usage of the vm
     * @return 
     */
    VmFairshareRecord createRecord(VmElement vm, float usage);
    
    /**
     * Creates a new VmElement by updating the data in the inputted 
     * VmElement from the given History Record.
     * 
     * @param vm the VmElement we want to update
     * @param record the history record used to update the data in vm
     * @return the updated VmElement
     */
    VmElement createVmFromRecord(VmElement vm, VmFairshareRecord record);
    
    /**
     * Removes all the stored VM records.
     */
    void clearContent();
}
