/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.scheduler.fairshare.calculators;

import cz.muni.fi.scheduler.elementpools.IDatastorePool;
import cz.muni.fi.scheduler.elementpools.IHostPool;
import cz.muni.fi.scheduler.resources.DatastoreElement;
import cz.muni.fi.scheduler.resources.HostElement;
import cz.muni.fi.scheduler.resources.VmElement;

/**
 * This class calculates the penalty of a Virtual Machine by comparing the
 * desired resources to the total amount of resources in the system.
 * 
 * @author Andras Urge
 */
public class ProcessorEquivalentCalculator implements IVmPenaltyCalculator {
    
    private IHostPool hostPool;  
    private IDatastorePool dsPool;  
    
    private Float availableCpu;    
    private Integer availableMemory;
    private Integer availableStorageCapacity;

    public ProcessorEquivalentCalculator(IHostPool hostPool, IDatastorePool dsPool) {        
        this.hostPool = hostPool;
        availableCpu = getAvailableCpu();
        availableMemory = getAvailableMemory();
        availableStorageCapacity = getAvailableStorageCapacity();
    }
    
    @Override
    public float getPenalty(VmElement vm) {
        float maxResource = Math.max(Math.max(
                                vm.getCpu()/availableCpu, 
                                ((float)vm.getMemory())/availableMemory),
                                ((float)vm.getDiskSizes())/availableStorageCapacity);
        return maxResource * availableCpu;
    }

    /**
     * Calculates the total amount of CPU in the system.
     * 
     * @return Total amount of CPU in the system
     */
    private float getAvailableCpu() {
        float cpu = 0;
        for (HostElement host : hostPool.getHosts()) {
            cpu += host.getMax_cpu();
        }
        return cpu;
    }

    /**
     * Calculates the total amount of RAM in the system.
     * 
     * @return Total amount of RAM in the system
     */
    private int getAvailableMemory() {
        int memory = 0;
        for (HostElement host : hostPool.getHosts()) {
            memory += host.getMax_mem();
        }
        return memory;
    }  
    
    private Integer getAvailableStorageCapacity() {
        int storage = 0;
        for (DatastoreElement ds : dsPool.getSystemDs()) {
            storage += ds.getTotal_mb();
        }
        return storage;
    }}
