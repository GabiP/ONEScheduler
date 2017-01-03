package cz.muni.fi.one.mappers;

import cz.muni.fi.one.XpathLoader;
import cz.muni.fi.scheduler.elements.nodes.DiskNode;
import cz.muni.fi.scheduler.elements.nodes.HistoryNode;
import cz.muni.fi.scheduler.elements.nodes.NicNode;
import cz.muni.fi.scheduler.elements.nodes.PciNode;
import cz.muni.fi.scheduler.elements.VmElement;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.opennebula.client.vm.VirtualMachine;

/**
 * This class maps OpenNebula's VirtualMachine class to VmElement class.
 * Retreives from OpenNebula's VirtualMachine instance its attributes by using OpenNebula's Java API.
 * For further information of Java API please refer to: https://docs.opennebula.org/5.2/integration/system_interfaces/java.html
 * 
 * @author Gabriela Podolnikova
 */
public class VmMapper {
    public static VmElement map(VirtualMachine vm) {
        VmElement result = new VmElement();
        vm.info();
        
        result.setVmId(XpathLoader.getInt(vm, "/VM/ID"));
        result.setUid(XpathLoader.getInt(vm, "/VM/UID"));
        result.setGid(XpathLoader.getInt(vm, "/VM/GID"));
        result.setName(vm.xpath("/VM/NAME"));
        result.setState(XpathLoader.getInt(vm, "/VM/STATE"));
        result.setLcm_state(XpathLoader.getInt(vm, "/VM/LCM_STATE"));
        result.setResched(XpathLoader.getInt(vm, "/VM/RESCHED"));
        result.setDeploy_id(vm.xpath("/VM/DEPLOY_ID"));
        result.setCpu(XpathLoader.getFloat(vm, "/VM/TEMPLATE/CPU"));
        result.setMemory(XpathLoader.getInt(vm, "/VM/TEMPLATE/MEMORY"));
        result.setSchedRank(vm.xpath("/VM/USER_TEMPLATE/SCHED_RANK"));
        result.setSchedDsRank(vm.xpath("/VM/USER_TEMPLATE/SCHED_DS_RANK"));
        result.setSchedRequirements(vm.xpath("/VM/USER_TEMPLATE/SCHED_REQUIREMENTS"));
        result.setSchedDsRequirements(vm.xpath("/VM/USER_TEMPLATE/SCHED_DS_REQUIREMENTS"));
        result.setTemplateId(XpathLoader.getInt(vm, "/VM/TEMPLATE/TEMPLATE_ID"));
                
        try {
            result.setDisks(XpathLoader.getNodeList(vm, DiskNode.class, "/VM/TEMPLATE/DISK"));
            result.setHistories(XpathLoader.getNodeList(vm, HistoryNode.class, "/VM/HISTORY_RECORDS/HISTORY"));
            result.setNics(XpathLoader.getNodeList(vm, NicNode.class, "/VM/TEMPLATE/NIC"));
            result.setPcis(XpathLoader.getNodeList(vm, PciNode.class, "/VM/TEMPLATE/PCI"));
        } catch (InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(VmMapper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }   
    
}
