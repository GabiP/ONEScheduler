package cz.muni.fi.scheduler.queues;

import cz.muni.fi.scheduler.resources.VmElement;
import cz.muni.fi.scheduler.setup.PropertiesConfig;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Gabriela Podolnikova
 */
public class FairshareMapper implements QueueMapper {
    
    protected final org.slf4j.Logger log = LoggerFactory.getLogger(getClass());
    
    @Override
    public List<Queue> mapQueues(List<VmElement> vms) {
        List<Queue> output = new ArrayList<>();
        int numberOfQueues = getNumberOfQueues();
        int numberOfVmsInQueue = (int) Math.ceil(vms.size()/ new Float(numberOfQueues));
        int startSub = 0;
        int endSub = numberOfVmsInQueue - 1;
        for (int i = 0; i < numberOfQueues; i++) {
            List<VmElement> vmsPartition  = vms.subList(startSub, endSub);
            Queue q = new Queue("Queue" + i, i, vmsPartition);
            log.info("A new queue was created: " + q);
            output.add(q);
            startSub = endSub + 1;
            endSub = endSub + numberOfVmsInQueue; 
        }
        return output;
    }

    @Override
    public int getNumberOfQueues() {
        try {
            PropertiesConfig properties = new PropertiesConfig("configuration.properties");
            return properties.getInt("numberofqueues");
        } catch (IOException ex) {
            Logger.getLogger(FairshareMapper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 1;
    }

}
