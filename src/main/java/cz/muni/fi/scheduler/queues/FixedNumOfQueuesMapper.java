package cz.muni.fi.scheduler.queues;

import cz.muni.fi.scheduler.elements.VmElement;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.LoggerFactory;

/**
 * This class implements the fixed number of queues policy.
 * @author Gabriela Podolnikova
 */
public class FixedNumOfQueuesMapper implements IQueueMapper {

    protected final org.slf4j.Logger log = LoggerFactory.getLogger(getClass());
    
    int numberOfQueues;
    
    public FixedNumOfQueuesMapper(int numberOfQueues) {
        this.numberOfQueues = numberOfQueues;
    }
    
    /**
     * Creates the number of queues given in the configuration file.
     * It takes VMs in not specified order and goes queue by queue until all pending VMs are in queues.
     * @param vms to be mapped ti queues
     * @return the list of queues.
     */
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
            Queue q = new Queue("Queue" + i, (float)i, emptyVms);
            output.add(q);
        }
        return output;
    }
    
    private void printQueues(List<Queue> output) {
        for (Queue q: output) {
            log.info("Created queue:" + q);
        }
    }
}
