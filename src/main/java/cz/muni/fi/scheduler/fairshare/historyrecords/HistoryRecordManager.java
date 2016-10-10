/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.scheduler.fairshare.historyrecords;

import cz.muni.fi.scheduler.resources.VmElement;
import cz.muni.fi.scheduler.resources.nodes.HistoryNode;

/**
 * The class responsible for handling the storage of virtual machine 
 * History Records.
 * 
 * @author Andras Urge
 */
public class HistoryRecordManager {
    
    // TODO: maybe change to JavaDB
    private static final String FILE_PATH = "historyRecords.txt";
    
    public static void insert(HistoryRecord record){
        // TODO: implement
    }
    
    public static HistoryRecord get(int vmId, int historySeq) {
        //TODO: implement
        return null;
    }  
    
    /**
     * Returns the History Record for the given virtual machine history. 
     * If it is not yet existing, a new one is created and stored.
     * 
     * @param vm The virtual machine of the history
     * @param history The history used to get the History Record
     * @return The history record
     */
    public static HistoryRecord loadHistoryRecord(VmElement vm, HistoryNode history) {
        HistoryRecord record = HistoryRecordManager.get(vm.getVmId(), history.getSequence());
        if (record == null) {
            record = new HistoryRecord(vm.getVmId(), history.getSequence(), vm.getCpu(), vm.getMemory());
            HistoryRecordManager.insert(record);
        }  
        return record;
    }
    
    /**
     * Creates a new VmElement by updating the data in the inputted 
     * VmElement from the given History Record.
     * 
     * @param vm The VmElement we want to update
     * @param record The history record used to update the data in vm
     * @return The updated VmElement
     */
    public static VmElement createVmFromHistory(VmElement vm, HistoryRecord record) {
        VmElement newVm = vm;
        newVm.setCpu(record.getCpu());
        newVm.setMemory(record.getMemory());
        return newVm;
    }
}
