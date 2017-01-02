/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.scheduler.fairshare.historyrecords;

import cz.muni.fi.scheduler.elements.VmElement;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The class responsible for storing fairshare records of virtual machines that
 * are not finished yet. The values are stored in a file.
 * 
 * @author Andras Urge
 */
public class VmFairshareRecordManager implements IVmFairshareRecordManager {
    
    private Properties properties = new Properties();
    private File file;

    public VmFairshareRecordManager(String filePath) {
        this.file = new File(filePath);
        if (file.exists()) {
            try (FileInputStream in = new FileInputStream(filePath)) {
                properties.load(in);
            } catch (IOException ex) {
                Logger.getLogger(UserFairshareRecordManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    /** 
     * Returns the record for the given VM.
     * 
     * @param vmId the ID of the vm
     * @return record for the vm
     */
    @Override
    public VmFairshareRecord getRecord(int vmId) {
        String recordData = properties.getProperty(Integer.toString(vmId));
        if (recordData == null) {
            return null;
        }
        String[] parts = recordData.split("\\|");
        int userId = Integer.parseInt(parts[0]);
        float vmPriority = Float.parseFloat(parts[1]);
        int lastClosedHistory = Integer.parseInt(parts[2]);
        float lastCpu = Float.parseFloat(parts[3]);
        int lastMemory = Integer.parseInt(parts[4]);
        int lastHdd = Integer.parseInt(parts[5]);
        
        return new VmFairshareRecord(vmId, userId, vmPriority, lastClosedHistory, lastCpu, lastMemory, lastHdd);
    }
    
    /** 
     * Returns the records for the VMs of the given user.
     * 
     * @param userId the ID of the user
     * @return records for the VMs of the user
     */
    @Override
    public List<VmFairshareRecord> getRecords(int userId) {
        List<VmFairshareRecord> userRecords = new ArrayList<>();
        for(String key : properties.stringPropertyNames()) {
            int vmId = Integer.parseInt(key);
            VmFairshareRecord record = getRecord(vmId);
            if (record.getUserId() == userId) {
                userRecords.add(record);
            }
        }
        return userRecords;
    }
    
    /**
     * Stores the given record.
     * 
     * @param record the record to be stored
     */
    @Override
    public void storeRecord(VmFairshareRecord record){
        String recordData = record.getUserId() + "|" + record.getUsage() + "|" 
                + record.getLastClosedHistory() + "|" + record.getLastCpu() + "|" + record.getLastMemory();
        properties.setProperty(Integer.toString(record.getVmId()), recordData);
        saveToFile();
    }    
    
    /**
     * Deletes the given record.
     * 
     * @param vmId ID of the record to be deleted
     */
    @Override
    public void delete(int vmId) {
        properties.remove(Integer.toString(vmId));
        saveToFile();
    }

    /**
     * Deletes the given records.
     * 
     * @param vmIds list of the IDs of the records to be deleted
     */
    @Override
    public void delete(List<Integer> vmIds) {
        for (int id : vmIds) {
            properties.remove(Integer.toString(id));
        }        
        saveToFile();
    }
    
    /**
     * Creates a record from the given VM with the specified fairshare usage.
     * 
     * @param vm the virtual machine
     * @param usage the fairshare usage of the vm
     * @return 
     */
    @Override
    public VmFairshareRecord createRecord(VmElement vm, float priority) {
        int lastClosedHistoryId;
        if (vm.getLastClosedHistory() == null) {
            lastClosedHistoryId = -1;
        } else {
            lastClosedHistoryId = vm.getLastClosedHistory().getSequence();
        }
        
        return new VmFairshareRecord(
                vm.getVmId(), vm.getUid(), priority, lastClosedHistoryId, vm.getCpu(), vm.getMemory(), vm.getDiskSizes());        
    }
    
    /**
     * Creates a new VmElement by updating the data in the inputted 
     * VmElement from the given History Record.
     * 
     * @param vm The VmElement we want to update
     * @param record The history record used to update the data in vm
     * @return The updated VmElement
     */
    @Override
    public VmElement createVmFromRecord(VmElement vm, VmFairshareRecord record) {
        vm.setCpu(record.getLastCpu());
        vm.setMemory(record.getLastMemory());
        return vm;
    }
    
    /**
     * Removes all the stored VM records.
     */
    @Override
    public void clearContent() {
        file.delete();
    }
    
    /**
     * Saves the values in the properties to the file.
     */
    private void saveToFile() {
        try (FileOutputStream fileOut = new FileOutputStream(file)) {
            properties.store(fileOut, "vmId=userId|vmPriority|lastClosedHistory|lastCpu|lastRAM|lastHDD");
        } catch (IOException ex) {
            Logger.getLogger(UserFairshareRecordManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
