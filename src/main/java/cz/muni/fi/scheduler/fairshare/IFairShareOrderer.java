/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.scheduler.fairshare;

import cz.muni.fi.scheduler.resources.VmElement;
import java.util.List;

/**
 *
 * @author andris
 */
public interface IFairShareOrderer {

    /**
     * Orders the inputted Virtual Machines based on the chosen
     * AbstractPriorityCalculator.
     *
     * @param vms The Virtual Machines to order
     * @return The ordered Virtual Machines
     */
    public List<VmElement> orderVms(List<VmElement> vms);
    
}
