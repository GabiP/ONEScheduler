/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.scheduler.fairshare.calculators;

import cz.muni.fi.scheduler.elementpools.IHostPool;
import cz.muni.fi.scheduler.elementpools.IVmPool;
import cz.muni.fi.scheduler.resources.HostElement;
import cz.muni.fi.scheduler.resources.VmElement;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 *
 * @author andris
 */
public class ProcessorEquivalentCalculatorTest {
    
    private ProcessorEquivalentCalculator calculator; 
    private IVmPool vmPool; 
    private IHostPool hostPool;     
    
    @Before
    public void setUp() {
        vmPool = mock(IVmPool.class);
        hostPool = mock(IHostPool.class);
        when(hostPool.getHosts()).thenReturn(getHosts());
        calculator = new ProcessorEquivalentCalculator(vmPool, hostPool, false);
    }

    @Test(expected = NullPointerException.class) 
    public void testGetPenalty_Null_ThrowsException() {   
        calculator.getPenalty(null);
    }
    
    @Test
    public void testGetPenalty_WithHighCpuReq() {
        VmElement vm = new VmElement();
        vm.setCpu(5f);
        vm.setMemory(256);
        
        float result = calculator.getPenalty(vm);
       
        double delta = 0.0001;               
        assertEquals("Penalty is not equal to CPU.", 5, result, delta);
    }
    
    @Test
    public void testGetPenalty_WithHighMemoryReq() {
        VmElement vm = new VmElement();
        vm.setCpu(1f);
        vm.setMemory(1024);
        
        float result = calculator.getPenalty(vm);
       
        double delta = 0.0001;               
        assertEquals("Penalty is not equal to CPU.", 4, result, delta);
    }
    
    private List<HostElement> getHosts() {
        List<HostElement> hosts = new ArrayList<>();
                
        hosts.add(createHostElement(2, 512));
        hosts.add(createHostElement(4, 1024));
        
        return hosts;
    }
    
    private HostElement createHostElement(float maxCpu, int maxMemory) {
        HostElement host = new HostElement();
        host.setMax_cpu(maxCpu);
        host.setMax_mem(maxMemory);
        return host;
    }
    
}