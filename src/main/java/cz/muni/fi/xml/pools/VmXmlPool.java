package cz.muni.fi.xml.pools;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import cz.muni.fi.scheduler.elementpools.IVmPool;
import cz.muni.fi.scheduler.elements.VmElement;
import cz.muni.fi.xml.mappers.VmXmlMapper;
import cz.muni.fi.xml.resources.lists.VmXmlList;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.mapstruct.factory.Mappers;

import org.opennebula.client.Pool;
import org.opennebula.client.vm.VirtualMachinePool;

/**
 *
 * @author Andras Urge
 */
public class VmXmlPool implements IVmPool {
    
    private List<VmElement> vms;

    VmXmlMapper vmXmlMapper = Mappers.getMapper(VmXmlMapper.class);
    
    private long usedMB;
    
    public VmXmlPool(String vmPoolPath) throws IOException {
        XmlMapper xmlMapper = new XmlMapper();
        String vmPoolMessage = new String(Files.readAllBytes(Paths.get(vmPoolPath)));
        VmXmlList xmlList = xmlMapper.readValue(vmPoolMessage, VmXmlList.class);
        System.gc();
        Runtime rt = Runtime.getRuntime();
        usedMB = (rt.totalMemory() - rt.freeMemory()) / 1024 / 1024; 
        vms = vmXmlMapper.map(xmlList.getVms());
    }
    
    @Override
    public VmElement getVm(int vmId) {
        for (VmElement vm : vms) {                       
            if (vmId == vm.getVmId()) {
                return vm;
            }
        }
        return null;
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
                if (state == VirtualMachinePool.NOT_DONE) {
                    hasState = (vm.getState() != 6);
                } else {
                    hasState = (vm.getState() == state);                 
                }
            }
            
            if (hasUser && hasState) {
                result.add(vm);
            }
        }
        
        return result;
    }

    @Override
    public List<VmElement> getReschedVms() {
        return vms.stream().filter(VmElement::isResched).collect(Collectors.toList());
    }

    public long getUsedMB() {
        return usedMB;
    }

    public void setUsedMB(long usedMB) {
        this.usedMB = usedMB;
    }
    
}
