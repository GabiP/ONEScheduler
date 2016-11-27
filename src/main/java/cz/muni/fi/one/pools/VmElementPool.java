package cz.muni.fi.one.pools;

import cz.muni.fi.one.mappers.VmMapper;
import cz.muni.fi.scheduler.elementpools.IVmPool;
import cz.muni.fi.scheduler.resources.VmElement;
import java.util.ArrayList;
import java.util.Iterator;
import org.opennebula.client.Client;
import org.opennebula.client.OneResponse;
import org.opennebula.client.Pool;
import org.opennebula.client.vm.VirtualMachine;
import org.opennebula.client.vm.VirtualMachinePool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class represents OpenNebula's VirtualMachinePool containing all instances of virtual machines in the system.
 * The pool is accessed through OpenNebula's Client. The Client represents the connection with the core of OpenNebula.
 * Each OpenNebula's instance of VirtualMachine is mapped to our VmElement.
 * 
 * @author Gabriela Podolnikova
 */
public class VmElementPool implements IVmPool{

    protected final Logger log = LoggerFactory.getLogger(getClass());
    
    private VirtualMachinePool vmp;

    public VmElementPool(Client oneClient) {
        vmp = new VirtualMachinePool(oneClient);
    }
    
    @Override
    public VmElement getVm(int vmId) {
        vmp.info();
        return VmMapper.map(vmp.getById(vmId));
    }
    
    @Override
    public ArrayList<VmElement> getVms() {
        return getVms(Pool.ALL, VirtualMachinePool.NOT_DONE); 
    }
    
    @Override
    public ArrayList<VmElement> getAllVms() {
        return getVms(Pool.ALL, VirtualMachinePool.ALL_VM); 
    }
    
    @Override
    public ArrayList<VmElement> getVmsByUser(int userId) {
        return getVms(userId, VirtualMachinePool.NOT_DONE); 
    }
    
    @Override
    public ArrayList<VmElement> getAllVmsByUser(int userId) {
        return getVms(userId, VirtualMachinePool.ALL_VM); 
    }
    
    @Override
    public ArrayList<VmElement> getVmsByState(int state) {
        return getVms(Pool.ALL, state); 
    }
    
    /**
     * Goes through the pool and maps all vms.
     * @param userId the id of the owner of the vms we want to obtain
     * @param state the state of the vms that we want to obtain
     * @return the list of VmElements
     */
    @Override
    public ArrayList<VmElement> getVms(int userId, int state) {
        ArrayList<VmElement> vms = new ArrayList<>();
        OneResponse vmpr = vmp.info(userId, Integer.MIN_VALUE, Integer.MAX_VALUE, state);
        if (vmpr.isError()) {
            log.error(vmpr.getErrorMessage());
        }
        Iterator<VirtualMachine> itr = vmp.iterator();
        while (itr.hasNext()) {
            VirtualMachine element = itr.next();
            VmElement vm = VmMapper.map(element);
            vms.add(vm);
        }
        return vms;
    }
}
