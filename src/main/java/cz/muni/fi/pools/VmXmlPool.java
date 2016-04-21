/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.pools;

import cz.muni.fi.resources.VmXml;
import java.util.ArrayList;
import java.util.Iterator;
import org.opennebula.client.Client;
import org.opennebula.client.OneResponse;
import org.opennebula.client.Pool;
import org.opennebula.client.vm.VirtualMachine;
import org.opennebula.client.vm.VirtualMachinePool;

/**
 *
 * @author Gabriela Podolnikova
 */
public class VmXmlPool {

    private VirtualMachinePool vmp;
    
    // TODO: add caching to improve performance

    public VmXmlPool(Client oneClient) {
        vmp = new VirtualMachinePool(oneClient);
    }
    
    public ArrayList<VmXml> getVms() {
        return getVms(Pool.ALL, VirtualMachinePool.NOT_DONE); 
    }
    
    public ArrayList<VmXml> getAllVms() {
        return getVms(Pool.ALL, VirtualMachinePool.ALL_VM); 
    }
    
    public ArrayList<VmXml> getVmsByUser(int userId) {
        return getVms(userId, VirtualMachinePool.NOT_DONE); 
    }
    
    public ArrayList<VmXml> getAllVmsByUser(int userId) {
        return getVms(userId, VirtualMachinePool.ALL_VM); 
    }
    
    public ArrayList<VmXml> getVmsByState(int state) {
        return getVms(Pool.ALL, state); 
    }
    
    /**
     * Gets virtual machines by user and state.
     * User ids:   >= 0            = UID User's Virtual Machines
     *             Pool.ALL        = All Virtual Machines
     *             Pool.MINE       = Connected user's Virtual Machines
     *             Pool.MINE_GROUP = Connected user's Virtual Machines, and the ones in his group
     *             
     * Vm states:  1 = pending
     *             2 = hold
     *             3 = active
     *             4 = stopped
     *             5 = suspended
     *             6 = done
     *             8 = poweroff
     *             9 = undeployed
     *             VirtualMachinePool.ALL_VM   = Flag for Virtual Machines in any state.
     *             VirtualMachinePool.NOT_DONE = Flag for Virtual Machines in any state, except for DONE.
     * @return array of virtual machines
     */
    public ArrayList<VmXml> getVms(int userId, int state) {
        ArrayList<VmXml> vms = new ArrayList<>();
        OneResponse vmpr = vmp.info(userId, Integer.MIN_VALUE, Integer.MAX_VALUE, state);
        if (vmpr.isError()) {
            //TODO: log it
            System.out.println(vmpr.getErrorMessage());
        }
        Iterator<VirtualMachine> itr = vmp.iterator();
        while (itr.hasNext()) {
            VirtualMachine element = itr.next();
            VmXml vm = new VmXml(element);
            vms.add(vm);
        }
        return vms;
    }    

    /**
     * @return the vmp
     */
    public VirtualMachinePool getVmp() {
        return vmp;
    }

    /**
     * @param vmp the vmp to set
     */
    public void setVmp(VirtualMachinePool vmp) {
        this.vmp = vmp;
    }
}
