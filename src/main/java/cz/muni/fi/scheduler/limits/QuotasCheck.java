package cz.muni.fi.scheduler.limits;

import cz.muni.fi.scheduler.core.Match;
import cz.muni.fi.scheduler.elementpools.IUserPool;
import cz.muni.fi.scheduler.resources.UserElement;
import cz.muni.fi.scheduler.resources.VmElement;
import cz.muni.fi.scheduler.resources.nodes.DatastoreQuota;
import cz.muni.fi.scheduler.resources.nodes.VmQuota;
import java.util.List;
import javax.inject.Inject;

/**
 *
 * @author Gabriela Podolnikova
 */
public class QuotasCheck implements LimitChecker {
    
    @Inject
    IUserPool userPool;
    
    //inject User quotas data - first create bean

    @Override
    public boolean checkLimit(VmElement vm, Match match) {
        UserElement user = userPool.getUser(vm.getUid());
        List<DatastoreQuota> dsQuotas = user.getDatastoreQuota();
        VmQuota vmQuota = user.getVmQuota();
        boolean dsQuotasCheck = checkDsQuotas(dsQuotas, vm, match);
        boolean vmQuotaCheck = checkVmQuota(vmQuota, vm);
        if (dsQuotasCheck && vmQuotaCheck) {
            return true;
        }
        return false;
    }

    private boolean checkDsQuotas(List<DatastoreQuota> quotas, VmElement vm, Match match) {
        for (DatastoreQuota q: quotas) {
            if (q.getId().equals(match.getDatastore().getId())) {
                return checkDsQuota(q, vm);
            }
        }
        return true;
    }
    
    private boolean checkDsQuota(DatastoreQuota q, VmElement vm) {
        Integer freeSize = q.getSize() - q.getSizeUsed();
        return freeSize >= vm.getDiskSizes();
    }
    
    private boolean checkVmQuota(VmQuota q, VmElement vm) {
        Float freeCpu = q.getCpu() - q.getCpuUsed();
        Integer freeMemory = q.getMemory() - q.getMemoryUsed();
        Integer freeDisk = q.getSystemDiskSize() - q.getSystemDiskSizeUsed();
        Integer freeVms = q.getVms() - q.getVmsUsed();
        return (checkUserCpu(freeCpu, vm) && checkUserMemory(freeMemory, vm) && checkUserDisk(freeDisk, vm) && checkUserVms(freeVms));
    }
    
    private boolean checkUserCpu(Float cpu, VmElement vm) {
        return (cpu >= vm.getCpu());
    }
    
    private boolean checkUserMemory(Integer memory, VmElement vm) {
        return (memory >= vm.getMemory());
    }
    
    private boolean checkUserDisk(Integer disk, VmElement vm) {
        return (disk >= vm.getDiskSizes());
    }
    
    private boolean checkUserVms(Integer vms) {
        return (vms >= 1);
    }
    
}
