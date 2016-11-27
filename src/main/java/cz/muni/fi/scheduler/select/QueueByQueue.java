package cz.muni.fi.scheduler.select;

import cz.muni.fi.scheduler.queues.Queue;
import cz.muni.fi.scheduler.resources.VmElement;
import java.util.List;

/**
 *
 * @author Gabriela Podolnikova
 */
public class QueueByQueue implements VmSelector {

    @Override
    public VmElement selectVm(List<Queue> queues) {
        return queues.get(0).getVms().get(0);
    }


}
