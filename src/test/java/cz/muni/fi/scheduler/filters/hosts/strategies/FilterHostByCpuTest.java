package cz.muni.fi.scheduler.filters.hosts.strategies;

import cz.muni.fi.scheduler.core.SchedulerData;
import cz.muni.fi.scheduler.elementpools.IClusterPool;
import cz.muni.fi.scheduler.elements.ClusterElement;
import cz.muni.fi.scheduler.elements.HostElement;
import cz.muni.fi.scheduler.elements.VmElement;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.when;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 *
 * @author Gabriela Podolnikova
 */
@RunWith(MockitoJUnitRunner.class)
public class FilterHostByCpuTest {

    private VmElement vm;
    private HostElement host;
    private ClusterElement cluster;
    
    private FilterHostByCpu filter;
    
    @Mock
    private SchedulerData schedulerData;
    
    @Mock
    IClusterPool clusterPool;
    
    @Before
    public void setUp() {
        vm = new VmElement();
        host = new HostElement();
        cluster = new ClusterElement();
        cluster.setId(100);
        host.setClusterId(100);
        filter = new FilterHostByCpu(clusterPool);
        when(clusterPool.getCluster(100)).thenReturn(cluster);
    }
    
    @Test
    public void vmFitOnHost() {
        vm.setCpu(0.1f);
        host.setMax_cpu(0.5f);
        host.setCpu_usage(0.0f);
        
        host.setReservedCpu(0.0f);
        cluster.setReservedCpu(0.0f);

        when(schedulerData.getReservedCpu(host)).thenReturn(new Float(0.2));

        assertTrue(filter.test(vm, host, schedulerData));
    }
    
    @Test
    public void vmDoesNotFitsOnHost() {
        vm.setCpu(0.1f);
        host.setMax_cpu(0.5f);
        host.setCpu_usage(0.0f);
        host.setReservedCpu(0.0f);
        cluster.setReservedCpu(0.0f);
        when(schedulerData.getReservedCpu(host)).thenReturn(new Float(0.5f));

        assertFalse(filter.test(vm, host, schedulerData));
    }
    
    
    @Test
    public void vmDoesNotFitsOnHost2() {
        vm.setCpu(0.1f);
        host.setMax_cpu(0.5f);
        host.setCpu_usage(0.2f);
        host.setReservedCpu(0.0f);
        cluster.setReservedCpu(0.0f);
        when(schedulerData.getReservedCpu(host)).thenReturn(new Float(0.3f));

        assertFalse(filter.test(vm, host, schedulerData));
    }
    
    @Test
    public void testReservationsOnHostFit() {
        vm.setCpu(0.1f);
        host.setMax_cpu(0.5f);
        host.setCpu_usage(0.0f);
        
        host.setReservedCpu(0.1f);
        cluster.setReservedCpu(0.0f);
        when(schedulerData.getReservedCpu(host)).thenReturn(new Float(0.0f));

        assertTrue(filter.test(vm, host, schedulerData));
    }
    
    @Test
    public void testReservationsOnHostDoesNotFit() {
        vm.setCpu(0.1f);
        host.setMax_cpu(0.5f);
        host.setCpu_usage(0.0f);
        
        host.setReservedCpu(0.5f);
        cluster.setReservedCpu(0.0f);
        when(schedulerData.getReservedCpu(host)).thenReturn(new Float(0.0f));

        assertFalse(filter.test(vm, host, schedulerData));
    }
    
    @Test
    public void testReservationsOnClusterFit() {
        vm.setCpu(0.1f);
        host.setMax_cpu(0.5f);
        host.setCpu_usage(0.0f);
        
        host.setReservedCpu(0.0f);
        cluster.setReservedCpu(0.2f);
        when(schedulerData.getReservedCpu(host)).thenReturn(new Float(0.0f));

        assertTrue(filter.test(vm, host, schedulerData));
    }
    
    @Test
    public void testReservationsOnClusterDoesNotFit() {
        vm.setCpu(0.1f);
        host.setMax_cpu(0.5f);
        host.setCpu_usage(0.0f);
        
        host.setReservedCpu(0.0f);
        cluster.setReservedCpu(0.3f);
        when(schedulerData.getReservedCpu(host)).thenReturn(new Float(0.2f));

        assertFalse(filter.test(vm, host, schedulerData));
    }
}
