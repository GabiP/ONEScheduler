package cz.muni.fi.scheduler.limits.data;

import cz.muni.fi.scheduler.elements.UserElement;
import cz.muni.fi.scheduler.elements.VmElement;

/**
 *
 * @author gabi
 */
public interface LimitCheckerData {
    
    void initData();
    
    void increaseData(VmElement vm);
}
