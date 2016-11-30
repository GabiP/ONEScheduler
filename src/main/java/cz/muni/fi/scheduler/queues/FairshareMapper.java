package cz.muni.fi.scheduler.queues;

import cz.muni.fi.scheduler.resources.VmElement;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.collections4.ListUtils;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Gabriela Podolnikova
 */
public class FairshareMapper implements QueueMapper {
    
    protected final org.slf4j.Logger log = LoggerFactory.getLogger(getClass());
    
    //nebudu znat number of queues, nejak spocitat z fairsharu
    int numberOfQueues;
    
    @Override
    public List<Queue> mapQueues(List<VmElement> vms) {
        List<Queue> output = new ArrayList<>();
        int numberOfVmsInQueue = (int) Math.ceil(vms.size()/ new Float(numberOfQueues));
        List<List<VmElement>> listOfVmsLists = ListUtils.partition(vms, numberOfVmsInQueue);
        for (int i = 0; i < numberOfQueues; i++) {
            List<VmElement> vmsPartition  = listOfVmsLists.get(i);
            Queue q = new Queue("Queue" + i, i, vmsPartition);
            log.info("A new queue was created: " + q);
            output.add(q);
        }
        return output;
    }
}
