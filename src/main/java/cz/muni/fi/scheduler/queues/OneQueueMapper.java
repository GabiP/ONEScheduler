package cz.muni.fi.scheduler.queues;

import cz.muni.fi.scheduler.elements.VmElement;
import java.util.ArrayList;
import java.util.List;

/**
 * This class creates one FIFO queue.
 * @author Gabriela Podolnikova
 */
public class OneQueueMapper implements IQueueMapper {

    /**
     * Maps VMs into queues in FIFO fashion.
     * @param vms the VMs to be mapped
     * @return the created queues
     */
    @Override
    public List<Queue> mapQueues(List<VmElement> vms) {
        List<Queue> result  = new ArrayList<>();
        Queue fifoQueue = new Queue("FifoQueue", 1.00f , vms);
        result.add(fifoQueue);
        return result;
    }

}
