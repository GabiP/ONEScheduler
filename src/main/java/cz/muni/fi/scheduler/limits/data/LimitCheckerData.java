package cz.muni.fi.scheduler.limits.data;

import cz.muni.fi.scheduler.resources.UserElement;
import cz.muni.fi.scheduler.resources.VmElement;

/**
 *
 * @author gabi
 */
public interface LimitCheckerData {
    
    void initData();
    
    void increaseData(VmElement vm);
}
