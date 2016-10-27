/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.scheduler.fairshare.historyrecords;

import cz.muni.fi.scheduler.resources.VmElement;
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
 * The class responsible for handling the storage of virtual machine 
 * History Records.
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
        
        return new VmFairshareRecord(vmId, userId, vmPriority, lastClosedHistory, lastCpu, lastMemory);
    }
    
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
    
    @Override
    public void storeRecord(VmFairshareRecord record){
        String recordData = record.getUserId() + "|" + record.getPriority() + "|" 
                + record.getLastClosedHistory() + "|" + record.getLastCpu() + "|" + record.getLastMemory();
        properties.setProperty(Integer.toString(record.getVmId()), recordData);
        saveToFile();
    }    
    
    @Override
    public void delete(int vmId) {
        properties.remove(Integer.toString(vmId));
        saveToFile();
    }

    @Override
    public void delete(List<Integer> vmIds) {
        for (int id : vmIds) {
            properties.remove(Integer.toString(id));
        }        
        saveToFile();
    }
    
    @Override
    public VmFairshareRecord createRecord(VmElement vm, float priority) {
        int lastClosedHistoryId;
        if (vm.getLastClosedHistory() == null) {
            lastClosedHistoryId = -1;
        } else {
            lastClosedHistoryId = vm.getLastClosedHistory().getSequence();
        }
        
        return new VmFairshareRecord(
                vm.getVmId(), vm.getUid(), priority, lastClosedHistoryId, vm.getCpu(), vm.getMemory());        
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
        VmElement newVm = vm;
        newVm.setCpu(record.getLastCpu());
        newVm.setMemory(record.getLastMemory());
        return newVm;
    }
    
    private void saveToFile() {
        try (FileOutputStream fileOut = new FileOutputStream(file)) {
            properties.store(fileOut, "vmId=userId|vmPriority|lastClosedHistory|lastCpu|lastRAM");
        } catch (IOException ex) {
            Logger.getLogger(UserFairshareRecordManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
