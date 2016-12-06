package cz.muni.fi.scheduler.queues;

import cz.muni.fi.scheduler.resources.VmElement;
import cz.muni.fi.scheduler.select.QueueFactory;
import java.util.List;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertEquals;

/**
 *
 * @author Gabriela Podolnikova
 */
public class FixedNumberOfQueuesMapperTest {

    private static final int NUMBER_OF_QUEUES = 4;
    private static final int NUMBER_OF_VMS = 5;
    
    List<VmElement> vms;
    
    FixedNumOfQueuesMapper fixedNumofQueuesMapper;
    
    @Before
    public void init() {
        fixedNumofQueuesMapper = new FixedNumOfQueuesMapper(NUMBER_OF_QUEUES);
        vms = QueueFactory.prepareVms(0, NUMBER_OF_VMS);
    }
    
    @Test
    public void mapQueuesTest() {
        fixedNumofQueuesMapper = new FixedNumOfQueuesMapper(NUMBER_OF_QUEUES);
        vms = QueueFactory.prepareVms(0, NUMBER_OF_VMS);
        
        List<Queue> queues = fixedNumofQueuesMapper.mapQueues(vms);
        Queue q1 = queues.get(0);
        VmElement firstVm = q1.getVmQueue().peek();
        Integer expectedId = 0;
        assertEquals(expectedId, firstVm.getVmId());
        
        Queue q2 = queues.get(1);
        VmElement secondVm = q2.getVmQueue().peek();
        expectedId = 1;
        assertEquals(expectedId, secondVm.getVmId());
    }
    
}
