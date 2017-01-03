package cz.muni.fi.scheduler.selectors;

import cz.muni.fi.scheduler.queues.Queue;
import cz.muni.fi.scheduler.elements.VmElement;

import java.util.List;

/**
 * This interface defines the order in which the VMs are selected to be processed.
 * @author Gabriela Podolnikova
 */
public interface IVmSelector {

    VmElement selectVm(List<Queue> queues);

}
