package cz.muni.fi.scheduler.select;

import cz.muni.fi.scheduler.queues.Queue;
import cz.muni.fi.scheduler.resources.VmElement;
import java.util.List;

/**
 *
 * @author Gabriela Podolnikova
 */
public class RoundRobin implements VmSelector {

    @Override
    public VmElement selectVm(List<Queue> queues) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


}
