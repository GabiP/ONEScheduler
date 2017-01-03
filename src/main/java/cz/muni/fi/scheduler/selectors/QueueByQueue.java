package cz.muni.fi.scheduler.selectors;

import cz.muni.fi.scheduler.queues.Queue;
import cz.muni.fi.scheduler.elements.VmElement;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class implement the Queue by Queue policy.
 * @author Gabriela Podolnikova
 */
public class QueueByQueue implements IVmSelector {
  
    protected final Logger log = LoggerFactory.getLogger(getClass());
    
    /**
     * Selects VM by VM in one queue. When the current queue is empty, it continues with the next queue.
     * It always dequeues the~first VM in the~first Queue until all queues are empty.
     * @param queues the queues
     * @return the selected VM
     */
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

}
