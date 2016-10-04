/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.scheduler.fairshare.calculators;

import cz.muni.fi.scheduler.elementpools.IHostPool;
import cz.muni.fi.scheduler.elementpools.IVmPool;
import cz.muni.fi.scheduler.fairshare.AbstractPriorityCalculator;
import cz.muni.fi.scheduler.resources.HostElement;
import cz.muni.fi.scheduler.resources.VmElement;

/**
 * This class calculates the penalty of a Virtual Machine by comparing the
 * desired resources to the total amount of resources in the system.
 * 
 * @author Andras Urge
 */
public class ProcessorEquivalentCalculator extends AbstractPriorityCalculator {
    
    private IHostPool hostPool;    
    
    private Float availableCpu;    
    private Integer availableMemory;

    public ProcessorEquivalentCalculator(IVmPool vmPool, IHostPool hostPool) {
        super(vmPool);
        this.hostPool = hostPool;
        availableCpu = getAvailableCpu();
        availableMemory = getAvailableMemory();
    }
    
    @Override
    protected float getPenalty(VmElement vm) {
        return Math.max(vm.getCpu()/availableCpu, ((float)vm.getMemory())/availableMemory) * availableCpu;
    }

    /**
     * Calculates the total amount of CPU in the system.
     * 
     * @return Total amount of CPU in the system
     */
    private float getAvailableCpu() {
        // TODO: maybe get from DB
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
        // TODO: maybe get from DB
        int memory = 0;
        for (HostElement host : hostPool.getHosts()) {
            memory += host.getMax_mem();
        }
        return memory;
    }  
}
