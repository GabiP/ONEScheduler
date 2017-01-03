/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.scheduler.fairshare.penaltycalculators;

import cz.muni.fi.scheduler.elements.VmElement;

/**
 * Interface for calculating virtual machine penalties.
 * 
 * @author Andras Urge
 */
public interface IVmPenaltyCalculator {
    
    /**
     * Returns a penalty calculated for the given virtual machine. The 
     * implementation  of this method is defining what kind of priorities 
     * will the users of the virtual machines get.
     * 
     * @param vm for which the penalty is calculated
     * @return penalty for the virtual machine
     */
    float getPenalty(VmElement vm);
}
