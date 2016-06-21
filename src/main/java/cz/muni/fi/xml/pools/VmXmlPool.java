/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.xml.pools;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import cz.muni.fi.scheduler.elementpools.IVmPool;
import cz.muni.fi.scheduler.resources.VmElement;
import cz.muni.fi.xml.mappers.VmXmlMapper;
import cz.muni.fi.xml.resources.VmJacksonPool;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.opennebula.client.Pool;
import org.opennebula.client.vm.VirtualMachinePool;

/**
 *
 * @author Andras Urge
 */
public class VmXmlPool implements IVmPool {

    List<VmElement> vms;
    
    public VmXmlPool(String xml) throws IOException {
        XmlMapper xmlMapper = new XmlMapper();
        VmJacksonPool vmPool = xmlMapper.readValue(xml, VmJacksonPool.class);
        vms = VmXmlMapper.map(vmPool.getVms());
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
            if (vm.getUid() == userId && vm.getState() == state) {
                result.add(vm);
            }
        }
        
        return result;
    }
    
}
