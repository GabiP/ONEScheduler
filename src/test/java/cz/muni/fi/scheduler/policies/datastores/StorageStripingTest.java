package cz.muni.fi.scheduler.policies.datastores;

import cz.muni.fi.scheduler.core.RankPair;
import cz.muni.fi.scheduler.core.SchedulerData;
import cz.muni.fi.scheduler.resources.DatastoreElement;
import cz.muni.fi.scheduler.resources.HostElement;
import cz.muni.fi.scheduler.resources.nodes.DatastoreNode;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.runners.MockitoJUnitRunner;

/**
 *
 * @author Gabriela Podolnikova
 */
@RunWith(MockitoJUnitRunner.class)
public class StorageStripingTest {
    List<DatastoreElement> datastores;
    
    List<DatastoreNode> datastoreNodes;
    
    DatastoreElement ds1;
    DatastoreElement ds2;
    DatastoreElement ds3;
    
    DatastoreNode dsNode1;
    
    HostElement host;

    @Mock
    private SchedulerData schedulerData;

    private StorageStriping policy;

    
    @Before
    public void init() {
        datastores = new ArrayList<>();
        datastoreNodes = new ArrayList<>();
        ds1 = new DatastoreElement();
        ds2 = new DatastoreElement();
        ds3 = new DatastoreElement();
        ds1.setTmMadName("ssh");
        ds2.setTmMadName("shared");
        ds3.setTmMadName("shared");
        dsNode1 = new DatastoreNode();
        host = new HostElement();
        policy = new StorageStriping();
        
        ds1.setId(1);
        ds2.setId(2);
        ds3.setId(3);
        dsNode1.setId_ds(1);
    }
    
    @Test
    public void testSelect() {               
        dsNode1.setFree_mb(100);
        ds2.setFree_mb(300);
        ds3.setFree_mb(100);
        
        datastores.add(ds1);
        datastores.add(ds2);
        datastores.add(ds3);
        datastoreNodes.add(dsNode1);
        
        host.setDatastores(datastoreNodes);
        
        when(schedulerData.getReservedStorage(ds1)).thenReturn(20);
        when(schedulerData.getReservedStorage(ds2)).thenReturn(0);
        when(schedulerData.getReservedStorage(ds3)).thenReturn(50);
        RankPair rankPair = policy.selectDatastore(datastores, host, schedulerData);

        assertEquals(ds2, rankPair.getDs());
    }
    
    @Test
    public void testSelectWhenLessHasNotSharedDs() {    
        ds2.setTmMadName("ssh");
        dsNode1.setFree_mb(100);
        ds2.setFree_mb(300);
        ds3.setFree_mb(100);
        
        datastores.add(ds1);
        datastores.add(ds2);
        datastores.add(ds3);
        datastoreNodes.add(dsNode1);
        
        host.setDatastores(datastoreNodes);
        
        when(schedulerData.getReservedStorage(ds1)).thenReturn(20);
        when(schedulerData.getReservedStorage(ds2)).thenReturn(0);
        when(schedulerData.getReservedStorage(ds3)).thenReturn(50);
        RankPair rankPair = policy.selectDatastore(datastores, host, schedulerData);
        
        //more free space has ds2, but it is not visible for host, so cannot make pair
        //so the more free space has ds1
        assertEquals(ds1, rankPair.getDs());
    }
}
