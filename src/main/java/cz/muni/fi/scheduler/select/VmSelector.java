package cz.muni.fi.scheduler.select;

import cz.muni.fi.scheduler.queues.Queue;
import cz.muni.fi.scheduler.resources.VmElement;
import java.util.List;

/**
 *
 * @author Gabriela Podolnikova
 */
public interface VmSelector {

    VmElement selectVm(List<Queue> queues);
}
