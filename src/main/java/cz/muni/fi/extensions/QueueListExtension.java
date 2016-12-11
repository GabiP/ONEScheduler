/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.extensions;

import cz.muni.fi.scheduler.queues.Queue;
import java.util.List;

/**
 *
 * @author Andras Urge
 */
public class QueueListExtension {
    
    public static Queue getQueueByName(List<Queue> queues, String name) {
        for (Queue q : queues) {
            if (q.getName().equals(name)) {
                return q;
            }
        }
        return null;
    }

    public static boolean queuesEmpty(List<Queue> queues) {
        for (Queue q: queues) {
            if (!q.isEmpty()) {
                return false;
            }
        }
        return true;
    }
}
