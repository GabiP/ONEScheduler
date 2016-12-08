package cz.muni.fi.scheduler.queues;

import cz.muni.fi.scheduler.elements.VmElement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Gabriela Podolnikova
 */
public class OneFifoQueue implements QueueMapper {

    @Override
    public List<Queue> mapQueues(List<VmElement> vms) {
        List<Queue> result  = new ArrayList<>();
        Queue fifoQueue = new Queue("FifoQueue", 1.00f , vms);
        result.add(fifoQueue);
        return result;
    }

}
