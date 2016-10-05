/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.scheduler.fairshare.calculators;

import cz.muni.fi.scheduler.elementpools.IVmPool;
import cz.muni.fi.scheduler.fairshare.AbstractPriorityCalculator;
import cz.muni.fi.scheduler.resources.VmElement;

/**
 * This class calculates the penalty of a Virtual Machine by the amount of 
 * CPU it requires.
 * 
 * @author Andras Urge
 */
public class MaxMinCalculator extends AbstractPriorityCalculator {
    
    public MaxMinCalculator(IVmPool vmPool, boolean useHistoryRecords) {
        super(vmPool, useHistoryRecords);
    }    
    
    @Override
    protected float getPenalty(VmElement vm) {
        return vm.getCpu();
    }    
}
