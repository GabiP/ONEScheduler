package cz.muni.fi.scheduler.select;

import cz.muni.fi.scheduler.selectors.QueueByQueue;
import cz.muni.fi.scheduler.queues.Queue;
import cz.muni.fi.scheduler.elements.VmElement;
import java.util.List;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 *
 * @author Gabriela Podolnikova
 */
public class QueueByQueueTest {
    
    QueueByQueue queueByQueue;
    
    List<Queue> queues;

    @Before
    public void init() {
        queueByQueue = new QueueByQueue();
        queues = QueueFactory.prepareQueues(3, 5);
    }
    
    @Test
    public void selecVmTest() {
        for (Queue q: queues) {
            System.out.println(q);
        }
        VmElement selectedVm = queueByQueue.selectVm(queues);
        Integer expectedId = 0;
        assertEquals(expectedId, selectedVm.getVmId());
        selectedVm = queueByQueue.selectVm(queues);
        expectedId = 1;
        assertEquals(expectedId, selectedVm.getVmId());
    }
    
}
