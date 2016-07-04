/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.xml.pools;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import cz.muni.fi.scheduler.elementpools.IVmPool;
import cz.muni.fi.scheduler.resources.VmElement;
import cz.muni.fi.xml.mappers.VmXmlMapper;
import cz.muni.fi.xml.resources.VmXml;
import java.util.ArrayList;
import java.util.List;
import org.opennebula.client.Pool;
import org.opennebula.client.vm.VirtualMachinePool;

/**
 *
 * @author Andras Urge
 */
@JacksonXmlRootElement(localName = "VMPOOL")
public class VmXmlPool implements IVmPool {
    
    @JacksonXmlProperty(localName = "VM")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<VmXml> vmXmls;
    
    private List<VmElement> vms;

    public VmXmlPool() {
        vms = VmXmlMapper.map(vmXmls);
    }
        
    @Override
    public List<VmElement> getVms() {
        return getVms(Pool.ALL, VirtualMachinePool.NOT_DONE);
    }

    @Override
    public List<VmElement> getAllVms() {
        return getVms(Pool.ALL, VirtualMachinePool.ALL_VM);
    }

    @Override
    public List<VmElement> getVmsByUser(int userId) {
        return getVms(userId, VirtualMachinePool.NOT_DONE);
    }

    @Override
    public List<VmElement> getAllVmsByUser(int userId) {
        return getVms(userId, VirtualMachinePool.ALL_VM); 
    }

    @Override
    public List<VmElement> getVmsByState(int state) {
        return getVms(Pool.ALL, state);
    }

    @Override
    public List<VmElement> getVms(int userId, int state) {
        List<VmElement> result = new ArrayList<>();
        for (VmElement vm : vms) {
            boolean hasUser = true;
            boolean hasState = true;
           
            if (userId != Pool.ALL) {
                hasUser = (vm.getUid() == userId);
            }
            
            if (state != VirtualMachinePool.ALL_VM) {
                hasState = (vm.getState() == state);
                if (state == VirtualMachinePool.NOT_DONE) {
                    hasState = hasState && (vm.getState() != 6);
                }
            }
            
            if (hasUser && hasState) {
                result.add(vm);
            }
        }
        
        return result;
    }
    
}
