/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.muni.fi.scheduler.queues;

import cz.muni.fi.scheduler.resources.VmElement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Gabriela Podolnikova
 */
public class QueuesExtensions {

    public static List<Queue> deleteVm(List<Queue> queues, VmElement vm) {
        List<Queue> result = new ArrayList<>(queues);
        Queue q = findQueue(queues, vm);
        if (q == null) {
            return null;
        }
        q.getVms().remove(vm);
        result.set(queues.indexOf(q), q);
        if (queueIsEmpty(q)) {
            result.remove(q);
        }
        return result;
    }
    
    private static Queue findQueue(List<Queue> queues, VmElement vm) {
        for (Queue q: queues) {
            if (q.getVms().contains(vm)) {
                return q;
            }
        }
        return null;
    }
    
    private static boolean queueIsEmpty(Queue q) {
        if (q.getVms().isEmpty()) {
            return true;
        }
        return false;
    }
    
}
