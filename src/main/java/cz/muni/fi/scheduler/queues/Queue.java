package cz.muni.fi.scheduler.queues;

import cz.muni.fi.scheduler.resources.VmElement;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 *
 * @author Gabriela Podolnikova
 */
public class Queue {
    
    private String name;
    private Integer priority;
    private ConcurrentLinkedQueue<VmElement> vmQueue = new ConcurrentLinkedQueue<>();

    public Queue(String name, Integer priority, List<VmElement> vms) {
        this.name = name;
        this.priority = priority;
        vmQueue.addAll(vms);
    }
    
    public VmElement dequeue() {
        return vmQueue.poll();
    }
    
    public boolean isEmpty() {
        return vmQueue.isEmpty();
    }
    
    @Override
    public String toString() {
        return "Queue{" + "name=" + name + ", priority=" + priority + ", vms=" + vmQueue + '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public ConcurrentLinkedQueue<VmElement> getVmQueue() {
        return vmQueue;
    }

    public void setVmQueue(ConcurrentLinkedQueue<VmElement> vmQueue) {
        this.vmQueue = vmQueue;
    }
    
    

}
