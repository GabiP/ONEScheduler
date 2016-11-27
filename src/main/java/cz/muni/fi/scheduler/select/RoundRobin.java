package cz.muni.fi.scheduler.select;

import cz.muni.fi.scheduler.queues.Queue;
import cz.muni.fi.scheduler.resources.VmElement;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Gabriela Podolnikova
 */
public class RoundRobin implements VmSelector {

    private int currentQueueIndex = 0; 
    
    @Override
    public VmElement selectVm(List<Queue> queues) {
        Queue currentQueue = queues.get(currentQueueIndex);
        while (currentQueue.isEmpty()) {
            currentQueueIndex++;
            checkLastIndex(queues.size());
            currentQueue = queues.get(currentQueueIndex);
        }
        VmElement vmToReturn = currentQueue.dequeue();
        currentQueueIndex++;
        checkLastIndex(queues.size());
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

    private void checkLastIndex(int size) {
        if (currentQueueIndex >= size) {
            currentQueueIndex = 0;
        }
    }
}
