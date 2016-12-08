package cz.muni.fi.scheduler.limits;

import cz.muni.fi.scheduler.core.Match;
import cz.muni.fi.scheduler.limits.data.LimitCheckerData;
import cz.muni.fi.scheduler.elements.VmElement;

/**
 *
 * @author Gabriela Podolnikova
 */
public interface LimitChecker {
    
    boolean checkLimit(VmElement vm, Match match);
    
    LimitCheckerData getDataInstance();
}
