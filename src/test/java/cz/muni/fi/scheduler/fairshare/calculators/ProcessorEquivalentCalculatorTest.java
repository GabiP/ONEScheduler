/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.scheduler.fairshare.calculators;

import cz.muni.fi.scheduler.resources.VmElement;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author andris
 */
public class ProcessorEquivalentCalculatorTest {
    
    public ProcessorEquivalentCalculatorTest() {
    }
    
    @Before
    public void setUp() {
    }

    @Test
    public void testGetPenalty() {
        System.out.println("getPenalty");
        VmElement vm = null;
        ProcessorEquivalentCalculator instance = null;
        float expResult = 0.0F;
        float result = instance.getPenalty(vm);
        assertEquals(expResult, result, 0.0);
        fail("The test case is a prototype.");
    }
    
}
