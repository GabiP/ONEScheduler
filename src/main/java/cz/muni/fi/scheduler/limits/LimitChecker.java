package cz.muni.fi.scheduler.limits;

import cz.muni.fi.scheduler.core.Match;
import cz.muni.fi.scheduler.resources.VmElement;

/**
 *
 * @author Gabriela Podolnikova
 */
public interface LimitChecker {
    
    boolean checkLimit(VmElement vm, Match match);
}
