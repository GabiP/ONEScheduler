/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.muni.fi.scheduler.filters.hosts.strategies;

import cz.muni.fi.scheduler.core.SchedulerData;
import cz.muni.fi.scheduler.elementpools.IClusterPool;
import cz.muni.fi.scheduler.resources.ClusterElement;
import cz.muni.fi.scheduler.resources.HostElement;
import cz.muni.fi.scheduler.resources.VmElement;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import static org.mockito.Mockito.mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 *
 * @author Gabriela Podolnikova
 */
@RunWith(MockitoJUnitRunner.class)
public class FilterHostByMemoryTest {
   
    private VmElement vm;
    private HostElement host;
    private ClusterElement cluster;
    private FilterHostByMemory filter;
    
    @Mock
    private SchedulerData schedulerData;
    
    @Mock
    IClusterPool clusterPool;
    
    @Before
    public void setUp() {
        vm = new VmElement();
        host = new HostElement();
        cluster = new ClusterElement();
        cluster = new ClusterElement();
        cluster.setId(100);
        host.setClusterId(100);
        filter = new FilterHostByMemory(clusterPool);
        when(clusterPool.getCluster(100)).thenReturn(cluster);
    }
    
    @Test
    public void vmFitOnHost() {
        vm.setMemory(64);
        host.setMax_mem(512);
        host.setMem_usage(128);
        host.setReservedMemory(0);
        cluster.setReservedMemory(0);
        when(schedulerData.getReservedMemory(host)).thenReturn(128);

        assertTrue(filter.test(vm, host, schedulerData));
    }
    
    @Test
    public void vmDoesNotFitsOnHost() {
        vm.setMemory(64);
        host.setMax_mem(128);
        host.setMem_usage(64);
        host.setReservedMemory(0);
        cluster.setReservedMemory(0);
        when(schedulerData.getReservedMemory(host)).thenReturn(64);

        assertFalse(filter.test(vm, host, schedulerData));
    }
    
    
    @Test
    public void vmDoesNotFitsOnHost2() {
        vm.setMemory(64);
        host.setMax_mem(128);
        host.setMem_usage(128);
        host.setReservedMemory(0);
        cluster.setReservedMemory(0);
        when(schedulerData.getReservedMemory(host)).thenReturn(0);

        assertFalse(filter.test(vm, host, schedulerData));
    }
    
    @Test
    public void testReservationsOnHostFit() {
        vm.setMemory(64);
        host.setMax_mem(512);
        host.setMem_usage(128);
        host.setReservedMemory(64);
        cluster.setReservedMemory(0);
        when(schedulerData.getReservedMemory(host)).thenReturn(0);

        assertTrue(filter.test(vm, host, schedulerData));
    }
    
    @Test
    public void testReservationsOnHostDoesNotFit() {
        vm.setMemory(64);
        host.setMax_mem(256);
        host.setMem_usage(64);
        host.setReservedMemory(64);
        cluster.setReservedMemory(0);
        when(schedulerData.getReservedMemory(host)).thenReturn(128);

        assertFalse(filter.test(vm, host, schedulerData));
    }
    
    @Test
    public void testReservationsOnClusterFit() {
        vm.setMemory(64);
        host.setMax_mem(512);
        host.setMem_usage(128);
        host.setReservedMemory(0);
        cluster.setReservedMemory(256);
        when(schedulerData.getReservedMemory(host)).thenReturn(0);

        assertTrue(filter.test(vm, host, schedulerData));
    }
    
    @Test
    public void testReservationsOnClusterDoesNotFit() {
        vm.setMemory(64);
        host.setMax_mem(512);
        host.setMem_usage(128);
        host.setReservedMemory(0);
        cluster.setReservedMemory(256);
        when(schedulerData.getReservedMemory(host)).thenReturn(128);

        assertFalse(filter.test(vm, host, schedulerData));
    }
    
}
