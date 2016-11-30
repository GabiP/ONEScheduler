/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.muni.fi.scheduler.filters.datastores.strategies;

import cz.muni.fi.scheduler.core.SchedulerData;
import cz.muni.fi.scheduler.resources.DatastoreElement;
import cz.muni.fi.scheduler.resources.HostElement;
import cz.muni.fi.scheduler.resources.VmElement;
import cz.muni.fi.scheduler.resources.nodes.DatastoreNode;
import cz.muni.fi.scheduler.resources.nodes.DiskNode;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.when;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

/**
 *
 * @author Gabriela Podolnikova
 */
public class FilterDatastoresByStorageTest {

    private VmElement vm;
    private DatastoreElement dsShared;
    private DatastoreElement dsLocal1;
    private DatastoreElement dsLocal2;
    private HostElement host1;
    private HostElement host2;
    private SchedulerData schedulerData;

    private FilterDatastoresByStorage filter;

    List<DatastoreNode> datastores = new ArrayList<>();
    DatastoreNode dsNode1 = new DatastoreNode();
    DatastoreNode dsNode2 = new DatastoreNode();


    
    @Before
    public void setUp() {
        vm = new VmElement();
        dsShared = new DatastoreElement();
        dsLocal1 = new DatastoreElement();
        dsLocal2 = new DatastoreElement();
        host1 = new HostElement();
        host1.setId(1);
        host2 = new HostElement();
        host2.setId(2);
        filter = new FilterDatastoresByStorage();
        schedulerData = mock(SchedulerData.class);
        
        dsShared.setId(0);
        dsLocal1.setId(1);
        dsLocal2.setId(2);
        
        dsShared.setShared("YES");
        
        //setting state to monitored
        dsShared.setState(0);
        
        //setting same clusters for hosts and datastores
        host1.setClusterId(0);
        host2.setClusterId(0);        
        List<Integer> clustersIds = new ArrayList<>();
        clustersIds.add(0);
        dsShared.setClusters(clustersIds);       
        dsLocal1.setClusters(clustersIds);
        dsLocal2.setClusters(clustersIds);
        
        List<DiskNode> disks = new ArrayList<>();
        DiskNode disk1 = new DiskNode();
        disk1.setSize(200);
        disks.add(disk1);
        DiskNode disk2 = new DiskNode();
        disk2.setSize(100);
        disks.add(disk2);
        vm.setDisks(disks);
        

        dsNode1.setId_ds(1);
        dsNode2.setId_ds(2);
        
    }
    
    @Test
    public void testVmIsResched() {
        vm.setResched(1);
        assertTrue(filter.test(vm, dsShared, host1, schedulerData));
    }
    
    @Test
    public void testFitsLocal() {
        dsNode1.setFree_mb(500);
        dsNode2.setFree_mb(300);
        datastores.add(dsNode1);
        datastores.add(dsNode2);
        host2.setDatastores(datastores);
        
        when(schedulerData.getReservedStorage(dsLocal1)).thenReturn(new Integer(64));

        assertTrue(filter.test(vm, dsLocal1, host2, schedulerData));
    }
    
    @Test
    public void testNotTheRightLocalDsThatFits() {
        dsNode1.setFree_mb(300);
        dsNode2.setFree_mb(500);
        datastores.add(dsNode1);
        datastores.add(dsNode2);
        host2.setDatastores(datastores);
        
        //Calling the test with the first dsNode on host2, and it does not fit
        //On dsNode1 there is 300 mb free space, but 64 is reserved,
        //do the free space that is calculated is: 236 and the VM requires 300
        when(schedulerData.getReservedStorage(dsLocal1)).thenReturn(new Integer(64));

        assertFalse(filter.test(vm, dsLocal1, host2, schedulerData));
    }
    
    @Test
    public void testDoesNotFitsLocal() {
        dsNode1.setFree_mb(500);
        dsNode2.setFree_mb(300);
        datastores.add(dsNode1);
        datastores.add(dsNode2);
        host2.setDatastores(datastores);
        
        when(schedulerData.getReservedStorage(dsLocal1)).thenReturn(new Integer(64));

        assertTrue(filter.test(vm, dsLocal1, host2, schedulerData));
    }
    
    @Test
    public void testFitsShared() {
        dsShared.setFree_mb(500);
        when(schedulerData.getReservedStorage(dsShared)).thenReturn(new Integer(64));
        assertTrue(filter.test(vm, dsShared, host1, schedulerData));
    }
    
    @Test
    public void testFitsSharedWithAnotherHost() {
        dsShared.setFree_mb(500);
        when(schedulerData.getReservedStorage(dsShared)).thenReturn(new Integer(64));
        assertTrue(filter.test(vm, dsShared, host2, schedulerData));
    }
    
    @Test
    public void testDoesNotFitShared() {
        dsShared.setFree_mb(300);
        when(schedulerData.getReservedStorage(dsShared)).thenReturn(new Integer(64));
        assertFalse(filter.test(vm, dsShared, host1, schedulerData));
    }
    
    @Test
    public void testNotDsNodeOnHost() {
        dsNode1.setFree_mb(500);
        dsNode2.setFree_mb(300);
        datastores.add(dsNode1);
        host2.setDatastores(datastores);
        assertFalse(filter.test(vm, dsLocal2, host2, schedulerData));
    }
    
    @Test
    public void testSharedDsIsNotMonitored() {
        dsShared.setState(1);
        assertFalse(filter.test(vm, dsShared, host1, schedulerData));
    }   
        
    @Test
    public void testDoesNotHaveDatastores() {
        assertFalse(filter.test(vm, dsLocal1, host2, schedulerData));
    }
    
    @Test
    public void testNotOnSameCluster() {
        host1.setClusterId(1);
        assertFalse(filter.test(vm, dsShared, host1, schedulerData));
    }
    
}
