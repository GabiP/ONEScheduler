package cz.muni.fi.scheduler.queues;

import cz.muni.fi.scheduler.resources.VmElement;
import java.util.List;

/**
 *
 * @author Gabriela Podolnikova
 */
public class Queue {
    
    private String name;
    private Integer priority;
    private List<VmElement> vms;

    public Queue(String name, Integer priority, List<VmElement> vms) {
        this.name = name;
        this.priority = priority;
        this.vms = vms;
    }

    @Override
    public String toString() {
        return "Queue{" + "name=" + name + ", priority=" + priority + ", vms=" + vms + '}';
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

    public List<VmElement> getVms() {
        return vms;
    }

    public void setVms(List<VmElement> vms) {
        this.vms = vms;
    }
    
    

}
