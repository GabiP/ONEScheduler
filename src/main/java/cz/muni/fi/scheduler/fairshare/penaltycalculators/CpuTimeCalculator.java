/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.scheduler.fairshare.penaltycalculators;

import cz.muni.fi.scheduler.resources.VmElement;

/**
 * This class calculates the penalty of a Virtual Machine by the amount of 
 * CPU it requires.
 * 
 * @author Andras Urge
 */
public class CpuTimeCalculator implements IVmPenaltyCalculator {
        
    @Override
    public float getPenalty(VmElement vm) {
        return vm.getCpu();
    }    
}
