package cz.muni.fi.scheduler.queues;

import cz.muni.fi.scheduler.elementpools.IUserPool;
import cz.muni.fi.scheduler.elements.UserElement;
import cz.muni.fi.scheduler.elements.VmElement;
import cz.muni.fi.scheduler.select.QueueFactory;
import java.util.List;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.runners.MockitoJUnitRunner;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertEquals;

/**
 *
 * @author Gabriela Podolnikova
 */
@RunWith(MockitoJUnitRunner.class)
public class QueueByUserTest {

    private static final int NUMBER_OF_VMS = 7;
    
    List<VmElement> vms;
    
    @Mock
    IUserPool userPool;
    
    QueueByUserMapper queueByUser;
    
    UserElement user0 = new UserElement();
    UserElement user1 = new UserElement();
    UserElement user2 = new UserElement();
    
    @Before
    public void init() {
        queueByUser = new QueueByUserMapper(userPool);
        vms = QueueFactory.prepareVms(0, NUMBER_OF_VMS);
        user0.setId(0);
        user1.setId(1);
        user2.setId(2);
    }
    
    @Test
    public void mapQueuestest() {
        vms.get(0).setUid(0);
        vms.get(1).setUid(1);
        vms.get(2).setUid(2);
        vms.get(3).setUid(0);
        vms.get(4).setUid(2);
        vms.get(5).setUid(2);
        vms.get(6).setUid(0);
        when(userPool.getUser(0)).thenReturn(user0);
        when(userPool.getUser(1)).thenReturn(user1);
        when(userPool.getUser(2)).thenReturn(user2);
        List<Queue> queues = queueByUser.mapQueues(vms);
        
        Queue q1 = queues.get(0);
        VmElement firstVm = q1.getVmQueue().peek();
        Integer expectedId = 0;
        assertEquals(expectedId, firstVm.getVmId());
        
        Queue q2 = queues.get(1);
        VmElement secondVm = q2.getVmQueue().peek();
        expectedId = 1;
        assertEquals(expectedId, secondVm.getVmId());
        
        Queue q3 = queues.get(2);
        VmElement thirdVm = q3.getVmQueue().peek();
        expectedId = 2;
        assertEquals(expectedId, thirdVm.getVmId());
    }
    
}
