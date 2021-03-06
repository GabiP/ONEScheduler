package cz.muni.fi.scheduler.limits.data;

import cz.muni.fi.scheduler.elementpools.IUserPool;
import cz.muni.fi.scheduler.elements.UserElement;
import cz.muni.fi.scheduler.elements.VmElement;
import java.util.HashMap;
import java.util.Map;

/**
 * This interface represents the current data for quotas checking.
 * @author Gabriela Podolnikova
 */
public class UserQuotasData implements LimitCheckerData {
    
    private Map<UserElement, Integer> datastoreSizeQuotas;
    
    private Map<UserElement, Float> cpuQuota;
    
    private Map<UserElement, Integer> memoryQuota;
    
    private Map<UserElement, Integer> diskQuota;
    
    private Map<UserElement, Integer> vmsQuota;
    
    IUserPool userPool;
    
    public UserQuotasData(IUserPool userPool) {
        this.userPool = userPool;
        initData();
    }
    
    @Override
    public void initData() {
        datastoreSizeQuotas = new HashMap<>();
        cpuQuota = new HashMap<>();
        memoryQuota = new HashMap<>();
        diskQuota = new HashMap<>();
        vmsQuota = new HashMap<>();
    }
    
    @Override
    public void increaseData(VmElement vm) {
        UserElement user = userPool.getUser(vm.getUid());
        datastoreSizeQuotas = addDatastoreSizeQuota(user, vm);
        cpuQuota = addCpuQuota(user, vm);
        memoryQuota = addMemoryQuota(user, vm);
        diskQuota = addDiskQuota(user, vm);
        vmsQuota = addVmQuota(user);
    }
      
    public Map<UserElement, Integer> addDatastoreSizeQuota(UserElement user, VmElement vm) {
        if (datastoreSizeQuotas.containsKey(user)) {
            datastoreSizeQuotas.replace(user, datastoreSizeQuotas.get(user), datastoreSizeQuotas.get(user) + vm.getDiskSizes());
        } else {
            datastoreSizeQuotas.put(user, vm.getDiskSizes());
        }
        return datastoreSizeQuotas;
    }
    
    public Map<UserElement, Float> addCpuQuota(UserElement user, VmElement vm) {
        if (cpuQuota.containsKey(user)) {
            cpuQuota.replace(user, cpuQuota.get(user), cpuQuota.get(user) + vm.getCpu());
        } else {
            cpuQuota.put(user, vm.getCpu());
        }
        return cpuQuota;
    }
    
    public Map<UserElement, Integer> addMemoryQuota(UserElement user, VmElement vm) {
        if (memoryQuota.containsKey(user)) {
            memoryQuota.replace(user, memoryQuota.get(user), memoryQuota.get(user) + vm.getMemory());
        } else {
            memoryQuota.put(user, vm.getMemory());
        }
        return memoryQuota;
    }
    
    public Map<UserElement, Integer> addDiskQuota(UserElement user, VmElement vm) {
        if (diskQuota.containsKey(user)) {
            diskQuota.replace(user, diskQuota.get(user), diskQuota.get(user) + vm.getDiskSizes());
        } else {
            diskQuota.put(user, vm.getDiskSizes());
        }
        return diskQuota;
    }
    
    public Map<UserElement, Integer> addVmQuota(UserElement user) {
        if (vmsQuota.containsKey(user)) {
            vmsQuota.replace(user, vmsQuota.get(user), vmsQuota.get(user) + 1);
        } else {
            vmsQuota.put(user, 1);
        }
        return vmsQuota;
    }
    
    public Integer getDsSizeQuota(UserElement user) {
        if (datastoreSizeQuotas.containsKey(user)) {
            return datastoreSizeQuotas.get(user);
        }
        return 0;
    }
    
    public Float getCpuQuota(UserElement user) {
        if (cpuQuota.containsKey(user)) {
            return cpuQuota.get(user);
        }
        return 0.00f;
    }
    
    public Integer getMemoryQuota(UserElement user) {
        if (memoryQuota.containsKey(user)) {
            return memoryQuota.get(user);
        }
        return 0;
    }
    
    public Integer getDiskQuota(UserElement user) {
        if (diskQuota.containsKey(user)) {
            return diskQuota.get(user);
        }
        return 0;
    }
    
    public Integer getVmsQuota(UserElement user) {
        if (vmsQuota.containsKey(user)) {
            return vmsQuota.get(user);
        }
        return 0;
    }
}
