package cz.muni.fi.scheduler.selectors;

import cz.muni.fi.scheduler.queues.Queue;
import cz.muni.fi.scheduler.elements.VmElement;

import java.util.List;

/**
 *
 * @author Gabriela Podolnikova
 */
public interface IVmSelector {

    VmElement selectVm(List<Queue> queues);

}
