package cz.muni.fi.scheduler.limits;

import cz.muni.fi.scheduler.limits.data.UserQuotasData;
import cz.muni.fi.scheduler.core.Match;
import cz.muni.fi.scheduler.elementpools.IUserPool;
import cz.muni.fi.scheduler.elements.DatastoreElement;
import cz.muni.fi.scheduler.elements.HostElement;
import cz.muni.fi.scheduler.elements.UserElement;
import cz.muni.fi.scheduler.elements.VmElement;
import cz.muni.fi.scheduler.elements.nodes.DatastoreQuota;
import cz.muni.fi.scheduler.elements.nodes.DiskNode;
import cz.muni.fi.scheduler.elements.nodes.VmQuota;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.runners.MockitoJUnitRunner;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 *
 * @author gabi
 */
@RunWith(MockitoJUnitRunner.class)
public class QuotasCheckTest {
    
    @Mock
    UserQuotasData userQuotasData;
    
    UserElement user;
    
    @Mock
    IUserPool userPool;
    
    List<DatastoreQuota> dsQuotas;
    
    VmQuota vmQuota;
    
    Match match;
    
    DatastoreElement ds;
    
    QuotasCheck quotasCheck;
    
    VmElement vm;
    
    @Before
    public void init() {
        vm = new VmElement();
        vm.setUid(1);
        user = new UserElement();
        ds = new DatastoreElement();
        dsQuotas = new ArrayList<>();
        quotasCheck = new QuotasCheck(userPool);
        
        user.setId(1);
        when(userPool.getUser(1)).thenReturn(user);      
        
        DatastoreQuota dsQuota = new DatastoreQuota();
        dsQuota.setId(1);
        dsQuota.setSize(1024);
        dsQuota.setSizeUsed(128);
        
        DatastoreQuota dsQuota1 = new DatastoreQuota();
        dsQuota1.setId(2);
        dsQuota1.setSize(1024);
        dsQuota1.setSizeUsed(1000);
        
        dsQuotas.add(dsQuota);
        dsQuotas.add(dsQuota1);
        user.setDatastoreQuota(dsQuotas);
        
        vmQuota = new VmQuota();
        vmQuota.setCpu(2.00f);
        vmQuota.setCpuUsed(0.00f);
        vmQuota.setMemory(1024);
        vmQuota.setMemoryUsed(0);
        vmQuota.setSystemDiskSize(512);
        vmQuota.setSystemDiskSizeUsed(0);
        vmQuota.setVms(5);
        vmQuota.setVmsUsed(0);
        user.setVmQuota(vmQuota);
        
        vm.setCpu(0.20f);
        vm.setMemory(128);
        List<DiskNode> disks = new ArrayList<>();
        DiskNode disk = new DiskNode();
        disk.setSize(128);
        disks.add(disk);
        vm.setDisks(disks);
    }
    
    @Test
    public void checkLimitTest() {
        ds.setId(1);
        match = new Match(new HostElement(), ds);
        assertTrue(quotasCheck.checkLimit(vm, match));
    }
    
    @Test
    public void checkLimitTestFalseCpu() {
        vmQuota.setCpu(1.00f);
        vm.setCpu(1.20f);
        ds.setId(1);
        match = new Match(new HostElement(), ds);
        assertFalse(quotasCheck.checkLimit(vm, match));
    }
    
    @Test
    public void checkLimitTestFalseMemory() {
        vmQuota.setMemory(128);
        vm.setMemory(215);
        ds.setId(1);
        match = new Match(new HostElement(), ds);
        assertFalse(quotasCheck.checkLimit(vm, match));
    }
    
    @Test
    public void checkLimitTestFalseDisk() {
        vmQuota.setSystemDiskSize(128);
        List<DiskNode> disks = new ArrayList<>();
        DiskNode disk = new DiskNode();
        disk.setSize(512);
        disks.add(disk);
        vm.setDisks(disks);
        ds.setId(1);
        match = new Match(new HostElement(), ds);
        assertFalse(quotasCheck.checkLimit(vm, match));
    }
    
    @Test
    public void checkLimitTestFalseVms() {
        vmQuota.setVms(5);
        vmQuota.setVmsUsed(5);
        ds.setId(1);
        match = new Match(new HostElement(), ds);
        assertFalse(quotasCheck.checkLimit(vm, match));
    }
    
    @Test
    public void checkLimitTestFalseDsQuota() {
        List<DiskNode> disks = new ArrayList<>();
        DiskNode disk = new DiskNode();
        disk.setSize(1012);
        disks.add(disk);
        vm.setDisks(disks);
        ds.setId(1);
        match = new Match(new HostElement(), ds);
        assertFalse(quotasCheck.checkLimit(vm, match));
    }
}
