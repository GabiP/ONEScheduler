package cz.muni.fi.scheduler.core;

import cz.muni.fi.scheduler.resources.DatastoreElement;
import cz.muni.fi.scheduler.resources.HostElement;
import cz.muni.fi.scheduler.resources.VmElement;
import cz.muni.fi.scheduler.resources.nodes.DatastoreNode;
import cz.muni.fi.scheduler.resources.nodes.DiskNode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import static org.junit.Assert.assertEquals;
import org.mockito.Mock;
import static org.mockito.Mockito.when;

/**
 *
 * @author Gabriela Podolnikova
 */
@RunWith(MockitoJUnitRunner.class)
public class SchedulerDataTest {

    private SchedulerData schedulerData;
    
    List<HostElement> hosts;

    Map<HostElement, Integer> runningVms = new HashMap<>();

    HostElement host1 = new HostElement();
    HostElement host2 = new HostElement();
    HostElement host3 = new HostElement();
    
    @Mock
    DatastoreElement ds;

    @Before
    public void init() {
        schedulerData = new SchedulerData();
        hosts = new ArrayList<>();

        host1.setId(1);
        host2.setId(2);
        host3.setId(3);
        
        host1.setRunningVms(0);
        host2.setRunningVms(2);
        host3.setRunningVms(3);
        
        hosts.add(host1);
        hosts.add(host2);
        hosts.add(host3);
        
        schedulerData.reserveHostRunningVm(host1);
        schedulerData.reserveHostRunningVm(host2);
    }
    @Test
    public void testGetActualRunninVms() {
        Map<HostElement, Integer> result = schedulerData.getActualRunningVms(hosts);
        assertEquals(Integer.valueOf(1), result.get(host1));
        assertEquals(Integer.valueOf(3), result.get(host2));
        assertEquals(Integer.valueOf(3), result.get(host3));
    }
    
    @Test
    public void reserveDatastoreNodeStorageTest() {
        HostElement host = new HostElement();
        DatastoreNode dsNode = new DatastoreNode();
        VmElement vm = new VmElement();
        
        List<DiskNode> disks = new ArrayList<>();
        DiskNode disk1 = new DiskNode();
        disk1.setSize(100);
        disks.add(disk1);
        
        host.setId(0);
        
        when(ds.getId()).thenReturn(0);
        dsNode.setId_ds(0);
        dsNode.setFree_mb(500);
        vm.setDisks(disks);
        
        Map<HostElement, Map<Integer, Integer>> reservedDsNodeStorage = schedulerData.reserveDatastoreNodeStorage(host, ds, vm);
        System.out.println(reservedDsNodeStorage.get(host));
    }
}
