/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.muni.fi.scheduler.filters.hosts.strategies;

import cz.muni.fi.scheduler.core.SchedulerData;
import cz.muni.fi.scheduler.resources.HostElement;
import cz.muni.fi.scheduler.resources.VmElement;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 *
 * @author Gabriela Podolnikova
 */
public class FilterHostByMemoryTest {
   
    private VmElement vm;
    private HostElement host;
    
    private FilterHostByMemory filter;
    
    private SchedulerData schedulerData;
    
    @Before
    public void setUp() {
        vm = new VmElement();
        host = new HostElement();
        filter = new FilterHostByMemory();
        schedulerData = mock(SchedulerData.class);
    }
    
    @Test
    public void vmFitOnHost() {
        vm.setMemory(64);
        host.setMax_mem(512);
        host.setMem_usage(128);
        
        when(schedulerData.getReservedMemory(host)).thenReturn(new Integer(128));

        assertTrue(filter.test(vm, host, schedulerData));
    }
    
    @Test
    public void vmDoesNotFitsOnHost() {
        vm.setMemory(64);
        host.setMax_mem(128);
        host.setMem_usage(64);
        
        when(schedulerData.getReservedMemory(host)).thenReturn(new Integer(64));

        assertFalse(filter.test(vm, host, schedulerData));
    }
    
    
    @Test
    public void vmDoesNotFitsOnHost2() {
        vm.setMemory(64);
        host.setMax_mem(128);
        host.setMem_usage(128);
        
        when(schedulerData.getReservedMemory(host)).thenReturn(new Integer(0));

        assertFalse(filter.test(vm, host, schedulerData));
    }
    
}
