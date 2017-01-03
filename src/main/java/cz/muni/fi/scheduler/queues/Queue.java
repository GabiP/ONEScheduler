package cz.muni.fi.scheduler.queues;

import cz.muni.fi.scheduler.elements.VmElement;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * A Queue is represented by its assigned VMs, priority and name.
 * Is used for grouping user's VMs by some defined policy.
 * 
 * @author Gabriela Podolnikova
 */
public class Queue {
    
    private String name;
    private Float priority;
    private ConcurrentLinkedQueue<VmElement> vmQueue = new ConcurrentLinkedQueue<>();

    public Queue(String name, Float priority, List<VmElement> vms) {
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
    
    public void queue(VmElement vm) {
        vmQueue.add(vm);
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

    public Float getPriority() {
        return priority;
    }

    public void setPriority(Float priority) {
        this.priority = priority;
    }

    public ConcurrentLinkedQueue<VmElement> getVmQueue() {
        return vmQueue;
    }

    public void setVmQueue(ConcurrentLinkedQueue<VmElement> vmQueue) {
        this.vmQueue = vmQueue;
    }
    
    

}
