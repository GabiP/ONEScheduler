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
public class VmFairshareRecord {
    
    private int vmId;
    
    private int userId;
    
    private float usage;
    
    private int lastClosedHistory;
    
    private float lastCpu;
    
    private int lastMemory;
    
    private int lastHdd;

    public VmFairshareRecord(int vmId, int userId, float usage, int lastClosedHistory, float lastCpu, int lastMemory, int lastHdd) {
        this.vmId = vmId;
        this.userId = userId;
        this.usage = usage;
        this.lastClosedHistory = lastClosedHistory;
        this.lastCpu = lastCpu;
        this.lastMemory = lastMemory;
        this.lastHdd = lastHdd;
    }
    
    public int getVmId() {
        return vmId;
    }

    public void setVmId(int vmId) {
        this.vmId = vmId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public float getUsage() {
        return usage;
    }

    public void setUsage(float usage) {
        this.usage = usage;
    }

    public int getLastClosedHistory() {
        return lastClosedHistory;
    }

    public void setLastClosedHistory(int lastClosedHistory) {
        this.lastClosedHistory = lastClosedHistory;
    }

    public float getLastCpu() {
        return lastCpu;
    }

    public void setLastCpu(float lastCpu) {
        this.lastCpu = lastCpu;
    }

    public int getLastMemory() {
        return lastMemory;
    }

    public void setLastMemory(int lastMemory) {
        this.lastMemory = lastMemory;
    }

    public int getLastHdd() {
        return lastHdd;
    }

    public void setLastHdd(int lastHdd) {
        this.lastHdd = lastHdd;
    }   
}
