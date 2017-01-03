package cz.muni.fi.extensions;

import cz.muni.fi.scheduler.queues.Queue;
import java.util.List;

/**
 * Class containing helper methods for a List of Queues.
 * 
 * @author Andras Urge
 */
public class QueueListExtension {
    
    /**
     * Returns the first Queue with the specified name.
     * 
     * @param queues the list of queues
     * @param name the name of the queue
     * @return the queue with the specified name
     */
    public static Queue getQueueByName(List<Queue> queues, String name) {
        for (Queue q : queues) {
            if (q.getName().equals(name)) {
                return q;
            }
        }
        return null;
    }

    /**
     * Checks whether all of the provided queues are empty.
     * 
     * @param queues the list of queues
     * @return true if all the queues are empty
     *         false otherwise
     */
    public static boolean queuesEmpty(List<Queue> queues) {
        for (Queue q: queues) {
            if (!q.isEmpty()) {
                return false;
            }
        }
        return true;
    }
}
