package cz.muni.fi.scheduler.limits;

import cz.muni.fi.scheduler.core.Match;
import cz.muni.fi.scheduler.limits.data.LimitCheckerData;
import cz.muni.fi.scheduler.limits.data.NoLimitData;
import cz.muni.fi.scheduler.elements.VmElement;

/**
 * This class is used when we do not want to check the limits.
 * @author Gabriela Podolnikova
 */
public class NoLimits implements ILimitChecker {

    NoLimitData noLimitData;
    
    public NoLimits() {
        noLimitData = new NoLimitData();
    }
    
    @Override
    public boolean checkLimit(VmElement vm, Match match) {
        return true;
    }

    @Override
    public LimitCheckerData getDataInstance() {
        return noLimitData;
    }

}
