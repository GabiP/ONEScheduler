package cz.muni.fi.scheduler.selectors;

import cz.muni.fi.scheduler.queues.Queue;
import cz.muni.fi.scheduler.elements.VmElement;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Gabriela Podolnikova
 */
public class QueueByQueue implements VmSelector {
  
    protected final Logger log = LoggerFactory.getLogger(getClass());
    
    @Override
    public VmElement selectVm(List<Queue> queues) {
        int i = 0;
        while (queues.get(i).isEmpty()) {
            i++;
        }
        VmElement vmToReturn = queues.get(i).dequeue();
        log.info("Dequeued queue: " + queues.get(i) + " Vm removed: " + vmToReturn);
        return vmToReturn;
    }
    
    @Override
    public boolean queuesEmpty(List<Queue> queues) {
        for (Queue q: queues) {
            if (!q.isEmpty()) {
                return false;
            }
        }       
        return true;
    }
}
