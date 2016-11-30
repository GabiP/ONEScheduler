/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.scheduler.elementpools;

import cz.muni.fi.scheduler.resources.VmElement;
import java.util.List;

/**
 *
 * @author Gabriela Podolnikova
 */
public interface IVmPool {    
    
    public VmElement getVm(int vmId);
    
    public List<VmElement> getVms();
    
    public List<VmElement> getAllVms();
    
    public List<VmElement> getVmsByUser(int userId);
    
    public List<VmElement> getAllVmsByUser(int userId);

    public List<VmElement> getVmsByState(int state);
    
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
    public List<VmElement> getVms(int userId, int state);
    
    public List<VmElement> getReschedVms();
}
