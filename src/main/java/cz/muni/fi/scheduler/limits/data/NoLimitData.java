package cz.muni.fi.scheduler.limits.data;

import cz.muni.fi.scheduler.resources.VmElement;

/**
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
