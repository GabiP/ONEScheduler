/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.muni.fi.scheduler.queues;

import cz.muni.fi.scheduler.resources.VmElement;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Gabriela Podolnikova
 */
public class FixedNumofQueuesMapper implements QueueMapper {

    protected final org.slf4j.Logger log = LoggerFactory.getLogger(getClass());
    
    int numberOfQueues;
    
    public FixedNumofQueuesMapper(int numberOfQueues) {
        this.numberOfQueues = numberOfQueues;
    }
    
    @Override
    public List<Queue> mapQueues(List<VmElement> vms) {
        List<Queue> output = createEmptyQueues();
        int indexOfQueue = 0;
        for (VmElement vm: vms)  {
            Queue q = output.get(indexOfQueue);
            q.queue(vm);
            indexOfQueue++;
            if (indexOfQueue >= numberOfQueues) {
                indexOfQueue = 0;
            }
        }
        printQueues(output);
        return output;
    }
        
    private List<Queue> createEmptyQueues() {
        List<Queue> output = new ArrayList<>();
        for (int i = 0; i< numberOfQueues; i++) {
            List<VmElement> emptyVms  = new ArrayList<>();
            Queue q = new Queue("Queue" + i, i, emptyVms);
            output.add(q);
        }
        return output;
    }
    
    private void printQueues(List<Queue> output) {
        for (Queue q: output) {
            log.info("Created queue:" + q);
        }
    }

    @Override
    public int getNumberOfQueues() {
        return numberOfQueues;
    }

}
