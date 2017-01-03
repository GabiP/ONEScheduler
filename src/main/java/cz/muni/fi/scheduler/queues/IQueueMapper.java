package cz.muni.fi.scheduler.queues;

import cz.muni.fi.scheduler.elements.VmElement;
import java.util.List;

/**
 * This interface is used for defining the queue mapping policies.
 * @author Gabriela Podolnikova
 */
public interface IQueueMapper {

    List<Queue> mapQueues(List<VmElement> vms);
}
