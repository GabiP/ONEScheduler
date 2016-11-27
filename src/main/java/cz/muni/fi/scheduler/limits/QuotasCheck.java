package cz.muni.fi.scheduler.limits;

import cz.muni.fi.scheduler.limits.data.UserQuotasData;
import cz.muni.fi.scheduler.core.Match;
import cz.muni.fi.scheduler.elementpools.IUserPool;
import cz.muni.fi.scheduler.limits.data.LimitCheckerData;
import cz.muni.fi.scheduler.resources.UserElement;
import cz.muni.fi.scheduler.resources.VmElement;
import cz.muni.fi.scheduler.resources.nodes.DatastoreQuota;
import cz.muni.fi.scheduler.resources.nodes.VmQuota;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Gabriela Podolnikova
 */
public class QuotasCheck implements LimitChecker {

    UserQuotasData userQuotasData;
    
    IUserPool userPool;
    
    protected final Logger log = LoggerFactory.getLogger(getClass());
    
    public QuotasCheck(IUserPool userPool) {
        this.userPool = userPool;
        userQuotasData = new UserQuotasData(userPool);
    }

    @Override
    public boolean checkLimit(VmElement vm, Match match) {
        UserElement user = userPool.getUser(vm.getUid());
        List<DatastoreQuota> dsQuotas = user.getDatastoreQuota();
        VmQuota vmQuota = user.getVmQuota();
        boolean dsQuotasCheck;
        if (dsQuotas.size() == 0) {
            dsQuotasCheck = true;
        } else {
            dsQuotasCheck = checkDsQuotas(dsQuotas, vm, match, user);
        }
        boolean vmQuotaCheck;
        if (vmQuota.isEmpty()) {
            vmQuotaCheck = true;
        } else {
            vmQuotaCheck = checkVmQuota(vmQuota, vm, user);
        }
        log.info("DsQuotaCheck result: " + dsQuotasCheck + " vmQuotaCheck result: " + vmQuotaCheck);
        return dsQuotasCheck && vmQuotaCheck;
    }
    
    @Override
    public LimitCheckerData getDataInstance() {
        return userQuotasData;
    }

    private boolean checkDsQuotas(List<DatastoreQuota> quotas, VmElement vm, Match match, UserElement user) {
        for (DatastoreQuota q: quotas) {
            if (q.getId().equals(match.getDatastore().getId())) {
                return checkDsQuota(q, vm, user);
            }
        }
        return true;
    }
    
    private boolean checkDsQuota(DatastoreQuota q, VmElement vm, UserElement user) {
        Integer freeSize = (q.getSize() - q.getSizeUsed()) + userQuotasData.getDsSizeQuota(user);
        return freeSize >= vm.getDiskSizes();
    }
    
    private boolean checkVmQuota(VmQuota q, VmElement vm, UserElement user) {
        Float freeCpu = (q.getCpu() - q.getCpuUsed()) + userQuotasData.getCpuQuota(user);
        Integer freeMemory = (q.getMemory() - q.getMemoryUsed()) + userQuotasData.getMemoryQuota(user);
        Integer freeDisk = (q.getSystemDiskSize() - q.getSystemDiskSizeUsed()) + userQuotasData.getDiskQuota(user);
        Integer freeVms = (q.getVms() - q.getVmsUsed()) + userQuotasData.getVmsQuota(user);
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
