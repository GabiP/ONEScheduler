/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.scheduler.fairshare.penaltycalculators;

import cz.muni.fi.scheduler.elements.VmElement;

/**
 *
 * @author andris
 */
public interface IVmPenaltyCalculator {
    
    /**
     * Returns a penalty calculated for the given virtual machine. The 
     * implementation  of this method is defining the strategy of the 
     * fair-share ordering.
     * 
     * @param vm for which the penalty is calculated
     * @return Penalty for the virtual machine
     */
    float getPenalty(VmElement vm);
}
