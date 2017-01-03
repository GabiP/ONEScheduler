package cz.muni.fi.one.pools;

import cz.muni.fi.one.mappers.VmMapper;
import cz.muni.fi.scheduler.elementpools.IVmPool;
import cz.muni.fi.scheduler.elements.VmElement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
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
    
    /**
     * Gets a virtual machine by the provided ID.
     * 
     * @param vmId the virtual machine ID
     * @return virtual machine
     */
    @Override
    public VmElement getVm(int vmId) {
        vmp.info();
        return VmMapper.map(vmp.getById(vmId));
    }
    
    /**
     * Gets all the virtual machines except the ones in state DONE.
     * 
     * @return all not done virtual machines
     */  
    @Override
    public ArrayList<VmElement> getVms() {
        return getVms(Pool.ALL, VirtualMachinePool.NOT_DONE); 
    }
    
    /**
     * Gets all the virtual machines.
     * 
     * @return all the virtual machines
     */
    @Override
    public ArrayList<VmElement> getAllVms() {
        return getVms(Pool.ALL, VirtualMachinePool.ALL_VM); 
    }
    
    /**
     * Gets the provided user's virtual machines except the ones in state DONE.
     * 
     * @param userId
     * @return user's not done virtual machines
     */
    @Override
    public ArrayList<VmElement> getVmsByUser(int userId) {
        return getVms(userId, VirtualMachinePool.NOT_DONE); 
    }
    
    /**
     * Gets the provided user's virtual machines.
     * 
     * @param userId
     * @return user's virtual machines
     */
    @Override
    public ArrayList<VmElement> getAllVmsByUser(int userId) {
        return getVms(userId, VirtualMachinePool.ALL_VM); 
    }
    
    /**
     * Gets all the virtual machines with the provided state.
     * 
     * @param state 
     * @return virtual machines with state
     */
    @Override
    public ArrayList<VmElement> getVmsByState(int state) {
        return getVms(Pool.ALL, state); 
    }
    
    /**
     * Gets virtual machines by user and state.
     * @param userId: >= 0            = UID User's Virtual Machines
     *             Pool.ALL        = All Virtual Machines
     *             
     * @param state: 1 = pending
     *             2 = hold
     *             3 = active
     *             4 = stopped
     *             5 = suspended
     *             6 = done
     *             8 = poweroff
     *             9 = undeployed
     *             VirtualMachinePool.ALL_VM   = Flag for Virtual Machines in any state.
     *             VirtualMachinePool.NOT_DONE = Flag for Virtual Machines in any state, except for DONE.
     * 
     * @return array of virtual machines
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

    /**
     * Goes through the pool and gets VMs with the rescheduling flag.
     * @return the list of VmElements to be rescheduled
     */
    @Override
    public List<VmElement> getReschedVms() {
        List<VmElement> vms = new ArrayList<>();
        Iterator<VirtualMachine> itr = vmp.iterator();
        while (itr.hasNext()) {
            VirtualMachine element = itr.next();
            VmElement vm = VmMapper.map(element);
            if (vm.isResched()) {
                vms.add(vm);
            }
        }
        return vms;
    }
}
