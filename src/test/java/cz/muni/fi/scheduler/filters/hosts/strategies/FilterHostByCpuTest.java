/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.muni.fi.scheduler.filters.hosts.strategies;

import cz.muni.fi.scheduler.SchedulerData;
import cz.muni.fi.scheduler.resources.HostElement;
import cz.muni.fi.scheduler.resources.VmElement;
import java.math.BigDecimal;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.mockito.AdditionalMatchers;
import org.mockito.ArgumentMatcher;
import org.mockito.ArgumentMatchers;
import static org.mockito.ArgumentMatchers.argThat;
import org.mockito.Mock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.mockito.stubbing.OngoingStubbing;

/**
 *
 * @author Gabriela Podolnikova
 */
public class FilterHostByCpuTest {

    private VmElement vm;
    private HostElement host;
    
    private FilterHostByCpu filter;
    
    private SchedulerData schedulerData;
    
    @Before
    public void setUp() {
        vm = new VmElement();
        host = new HostElement();
        filter = new FilterHostByCpu();
        schedulerData = mock(SchedulerData.class);
    }
    
    @Test
    public void vmFitOnHost() {
        vm.setCpu(0.1f);
        host.setMax_cpu(0.5f);
        host.setCpu_usage(0.0f);

        when(schedulerData.getReservedCpu(host)).thenReturn(new Float(0.2));

        assertTrue(filter.test(vm, host, schedulerData));
    }
    
    @Test
    public void vmDoesNotFitsOnHost() {
        vm.setCpu(0.1f);
        host.setMax_cpu(0.5f);
        host.setCpu_usage(0.0f);

        when(schedulerData.getReservedCpu(host)).thenReturn(new Float(0.5f));

        assertFalse(filter.test(vm, host, schedulerData));
    }
    
    
    @Test
    public void vmDoesNotFitsOnHost2() {
        vm.setCpu(0.1f);
        host.setMax_cpu(0.5f);
        host.setCpu_usage(0.2f);

        when(schedulerData.getReservedCpu(host)).thenReturn(new Float(0.3f));

        assertFalse(filter.test(vm, host, schedulerData));
    }
    
}
