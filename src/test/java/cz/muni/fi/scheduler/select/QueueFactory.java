package cz.muni.fi.scheduler.select;

import cz.muni.fi.scheduler.queues.Queue;
import cz.muni.fi.scheduler.elements.VmElement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Gabriela Podolnikova
 */
public class QueueFactory {

    public static List<Queue> prepareQueues(int numOfQueues, int numOfVms) {
        List<Queue> queues = new ArrayList<>();
        int indexStart = 0;
        for (int i = 0; i < numOfQueues; i++) {
            Queue q = new Queue("Q" + i, (float)i, prepareVms(indexStart, numOfVms));
            queues.add(q);
            indexStart = indexStart + numOfVms;
         }
        return queues;
    }
    
    public static List<VmElement> prepareVms(int indexStart, int numOfVms) {
        int endIndex = indexStart + numOfVms;
        List<VmElement> vms = new ArrayList<>();
        for (int i = indexStart; i < endIndex; i++) {
            VmElement vm = new VmElement();
            vm.setVmId(i);
            vms.add(vm);
        }
        System.out.println("Vms:" + vms);
        return vms;
    }
    
}
