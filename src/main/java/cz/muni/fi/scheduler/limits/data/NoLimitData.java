package cz.muni.fi.scheduler.limits.data;

import cz.muni.fi.scheduler.elements.VmElement;

/**
 * This class is used when we do not want to check the limits.
 * Represents the current data.
 * No data are being tracked.
 * 
 * @author Gabriela Podolnikova
 */
public class NoLimitData implements LimitCheckerData {

    @Override
    public void initData() {
    }

    @Override
    public void increaseData(VmElement vm) {
    }

}
