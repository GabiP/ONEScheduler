/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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

/**
 *
 * @author Gabriela Podolnikova
 */
public class VmElementPool implements IVmPool{

    private VirtualMachinePool vmp;
    
    // TODO: add caching to improve performance

    public VmElementPool(Client oneClient) {
        vmp = new VirtualMachinePool(oneClient);
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
    
    @Override
    public ArrayList<VmElement> getVms(int userId, int state) {
        ArrayList<VmElement> vms = new ArrayList<>();
        OneResponse vmpr = vmp.info(userId, Integer.MIN_VALUE, Integer.MAX_VALUE, state);
        if (vmpr.isError()) {
            //TODO: log it
            System.out.println(vmpr.getErrorMessage());
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
