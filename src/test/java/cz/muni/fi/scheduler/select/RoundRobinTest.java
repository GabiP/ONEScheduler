package cz.muni.fi.scheduler.select;

import cz.muni.fi.scheduler.selectors.RoundRobin;
import cz.muni.fi.scheduler.queues.Queue;
import cz.muni.fi.scheduler.elements.VmElement;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 *
 * @author Gabriela Podolnikova
 */
public class RoundRobinTest {

    RoundRobin roundRobin;
    
    List<Queue> queues;
    
    List<Queue> queues2;

    @Before
    public void init() {
        roundRobin = new RoundRobin();
        queues = QueueFactory.prepareQueues(3, 5);
    }
    
    @Test
    public void selecVmTest() {
        for (Queue q: queues) {
            System.out.println(q);
        }
        VmElement selectedVm = roundRobin.selectVm(queues);
        Integer expectedId = 0;
        assertEquals(expectedId, selectedVm.getVmId());
        selectedVm = roundRobin.selectVm(queues);
        expectedId = 5;
        assertEquals(expectedId, selectedVm.getVmId());
        selectedVm = roundRobin.selectVm(queues);
        expectedId = 10;
        assertEquals(expectedId, selectedVm.getVmId());
        selectedVm = roundRobin.selectVm(queues);
        expectedId = 1;
        assertEquals(expectedId, selectedVm.getVmId());
    }
    
    @Test
    public void selectVmTestEmptyQueues() {
        queues2 = new ArrayList<>();
        Queue q1 = new Queue("Q1", 1f, QueueFactory.prepareVms(0, 5));
        queues2.add(q1);
        Queue q2 = new Queue("Q2", 2f, QueueFactory.prepareVms(5, 0));
        queues2.add(q2);
        Queue q3 = new Queue("Q3", 3f, QueueFactory.prepareVms(5, 1));
        queues2.add(q3);
        Queue q4 = new Queue("Q4", 4f, QueueFactory.prepareVms(7, 2));
        queues2.add(q4);
        VmElement selectedVm = roundRobin.selectVm(queues2);
        Integer expectedId = 0;
        assertEquals(expectedId, selectedVm.getVmId());
        selectedVm = roundRobin.selectVm(queues2);
        expectedId = 5;
        assertEquals(expectedId, selectedVm.getVmId());
        selectedVm = roundRobin.selectVm(queues2);
        expectedId = 7;
        assertEquals(expectedId, selectedVm.getVmId());
        selectedVm = roundRobin.selectVm(queues2);
        expectedId = 1;
        assertEquals(expectedId, selectedVm.getVmId());
        selectedVm = roundRobin.selectVm(queues2);
        expectedId = 8;
        assertEquals(expectedId, selectedVm.getVmId());
        selectedVm = roundRobin.selectVm(queues2);
        expectedId = 2;
        assertEquals(expectedId, selectedVm.getVmId());
    }
    
}
