package cz.muni.fi.scheduler.queues;

import cz.muni.fi.scheduler.elements.VmElement;
import java.util.List;

/**
 *
 * @author Gabriela Podolnikova
 */
public interface QueueMapper {

    List<Queue> mapQueues(List<VmElement> vms);
}
