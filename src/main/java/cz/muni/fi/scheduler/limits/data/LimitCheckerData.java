package cz.muni.fi.scheduler.limits.data;

import cz.muni.fi.scheduler.elements.VmElement;

/**
 * This interface represents the current data, that are tracked in the limit implementation.
 * @author Gabriela Podolnikova
 */
public interface LimitCheckerData {
    
    void initData();
    
    void increaseData(VmElement vm);
}
