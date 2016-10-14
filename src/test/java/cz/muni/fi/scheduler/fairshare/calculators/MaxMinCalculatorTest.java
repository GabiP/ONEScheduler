/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.scheduler.fairshare.calculators;

import cz.muni.fi.scheduler.elementpools.IVmPool;
import cz.muni.fi.scheduler.resources.VmElement;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

/**
 *
 * @author Andras Urge
 */
public class MaxMinCalculatorTest {
    
    private MaxMinCalculator calculator; 
    private IVmPool vmPool; 
    
    @Before
    public void setUp() {        
        vmPool = mock(IVmPool.class);
        calculator = new MaxMinCalculator(vmPool, false);
    }

    @Test(expected = NullPointerException.class) 
    public void testGetPenalty_Null_ThrowsException() {   
        calculator.getPenalty(null);
    }
    
    @Test
    public void testGetPenalty_ReturnsCPU() {
        VmElement vm = new VmElement();
        vm.setCpu(0.1f);
       
        float result = calculator.getPenalty(vm);
       
        double delta = 0.0001;               
        assertEquals("Penalty is not equal to CPU.", 0.1f, result, delta);
    }
    
}
