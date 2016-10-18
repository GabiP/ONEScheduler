/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.scheduler.fairshare.historyrecords;

/**
 * This class represents the state of some parameters of a virtual machine 
 * at a specific time during its history. 
 * 
 * @author Andras Urge
 */
public class HistoryRecord {
    
    private int vmId;
    
    private int sequence;
    
    private float cpu;
    
    private int memory;

    public HistoryRecord(int vmId, int sequence, float cpu, int memory) {
        this.vmId = vmId;
        this.sequence = sequence;
        this.cpu = cpu;
        this.memory = memory;
    }    
    
    public int getVmId() {
        return vmId;
    }

    public void setVmId(int vmId) {
        this.vmId = vmId;
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public float getCpu() {
        return cpu;
    }

    public void setCpu(float cpu) {
        this.cpu = cpu;
    }

    public int getMemory() {
        return memory;
    }

    public void setMemory(int memory) {
        this.memory = memory;
    }
    
}
