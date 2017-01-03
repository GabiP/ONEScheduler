package cz.muni.fi.scheduler.elementpools;

import cz.muni.fi.scheduler.elements.VmElement;
import java.util.List;

/**
 * VM pool interface.
 * 
 * @author Gabriela Podolnikova
 */
public interface IVmPool {    
    
    /**
     * Gets a virtual machine by the provided ID.
     * 
     * @param vmId the virtual machine ID
     * @return virtual machine
     */
    VmElement getVm(int vmId);
    
    /**
     * Gets all the virtual machines except the ones in state DONE.
     * 
     * @return all not done virtual machines
     */
    List<VmElement> getVms();
    
    /**
     * Gets all the virtual machines.
     * 
     * @return all the virtual machines
     */
    List<VmElement> getAllVms();
    
    /**
     * Gets the provided user's virtual machines except the ones in state DONE.
     * 
     * @param userId
     * @return user's not done virtual machines
     */
    List<VmElement> getVmsByUser(int userId);
    
    /**
     * Gets the provided user's virtual machines.
     * 
     * @param userId
     * @return user's virtual machines
     */
    List<VmElement> getAllVmsByUser(int userId);

    /**
     * Gets all the virtual machines with the provided state.
     * 
     * @param state 
     * @return virtual machines with state
     */
    List<VmElement> getVmsByState(int state);
    
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
    List<VmElement> getVms(int userId, int state);
    
    /**
     * Gets VMs with resched flag set on true.
     * @return the VMs to be rescheduled.
     */
    List<VmElement> getReschedVms();
}
