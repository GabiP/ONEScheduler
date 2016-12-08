/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.xml.mappers;

import cz.muni.fi.scheduler.elements.VmElement;
import cz.muni.fi.scheduler.elements.nodes.DiskNode;
import cz.muni.fi.scheduler.elements.nodes.HistoryNode;
import cz.muni.fi.scheduler.elements.nodes.NicNode;
import cz.muni.fi.scheduler.elements.nodes.PciNode;
import cz.muni.fi.xml.resources.nodes.DiskNodeXml;
import cz.muni.fi.xml.resources.nodes.HistoryNodeXml;
import cz.muni.fi.xml.resources.nodes.NicNodeXml;
import cz.muni.fi.xml.resources.nodes.PciNodeXml;
import cz.muni.fi.xml.resources.VmXml;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Andras Urge
 */
public class VmXmlMapper {
    
    public static List<VmElement> map(List<VmXml> vms) {
        List<VmElement> result = new ArrayList<>();
        for(VmXml xml : vms) {
            result.add(map(xml));
        }        
        return result;
    }
    
    public static VmElement map(VmXml vm) {
        VmElement result = new VmElement();
        result.setVmId(vm.getVmId());
        result.setUid(vm.getUid());
        result.setGid(vm.getGid());
        result.setName(vm.getName());
        result.setState(vm.getState());
        result.setLcm_state(vm.getLcm_state());
        result.setResched(vm.getResched());
        result.setDeploy_id(vm.getDeploy_id());
        result.setCpu(vm.getCpu());
        result.setMemory(vm.getMemory());
        result.setTemplateId(vm.getTemplateId());
        result.setSchedRank(vm.getSchedRank());
        result.setSchedDsRank(vm.getSchedDsRank());
        result.setSchedRequirements(vm.getSchedRequirements());
        result.setSchedDsRequirements(vm.getSchedDsRequirements());
                
        result.setDisks(mapDisks(vm.getDisks()));
        result.setHistories(mapHistories(vm.getHistories()));
        result.setNics(mapNics(vm.getNics()));
        result.setPcis(mapPcis(vm.getPcis()));
        
        return result;
    }
    
    public static List<DiskNode> mapDisks(List<DiskNodeXml> disks) {
        List<DiskNode> result = new ArrayList<>();
        if (disks != null) {
            for(DiskNodeXml xml : disks) {
                result.add(map(xml));
            }        
        }
        return result;
    }
    
    public static DiskNode map(DiskNodeXml disk) {
        DiskNode result = new DiskNode();
        result.setDatastore_id(disk.getDatastore_id());
        result.setSize(disk.getSize());
        result.setClone(disk.getClone());
        result.setTmMadName(disk.getTmMadName());
        return result;
    }
    
    public static List<HistoryNode> mapHistories(List<HistoryNodeXml> histories) {
        List<HistoryNode> result = new ArrayList<>();
        if (histories != null) {
            for(HistoryNodeXml xml : histories) {
                result.add(map(xml));
            }        
        }
        return result;
    }
    
    public static HistoryNode map(HistoryNodeXml history) {
        HistoryNode result = new HistoryNode();
        result.setSequence(history.getSequence());        
        result.setStartTime(history.getStartTime());        
        result.setEndTime(history.getEndTime());        
        result.setReason(history.getReason());   
        return result;
    }
    
    public static List<NicNode> mapNics(List<NicNodeXml> nics) {
        List<NicNode> result = new ArrayList<>();
        if (nics != null) {
            for(NicNodeXml xml : nics) {
                result.add(map(xml));
            }   
        }
        return result;
    }
    
    public static NicNode map(NicNodeXml nic) {
        NicNode result = new NicNode();
        result.setNetworkId(nic.getNetworkId());           
        return result;
    }
    
    public static List<PciNode> mapPcis(List<PciNodeXml> pcis) {
        List<PciNode> result = new ArrayList<>();
        if (pcis != null) {
            for(PciNodeXml xml : pcis) {
                result.add(map(xml));
            }        
        }
        return result;
    }
    
    public static PciNode map(PciNodeXml pci) {
        PciNode result = new PciNode();
        result.setPci_class(pci.getPci_class());        
        result.setDevice(pci.getDevice());        
        result.setVendor(pci.getVendor());   
        
        return result;
    }
}
