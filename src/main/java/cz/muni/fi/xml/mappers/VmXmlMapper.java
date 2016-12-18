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
import java.util.List;
import org.mapstruct.Mapper;

/**
 *
 * @author Andras Urge
 */
@Mapper
public abstract class VmXmlMapper {
    
    public abstract List<VmElement> map(List<VmXml> vms);
    
    public abstract List<VmXml> mapToXml(List<VmElement> vms);
    
    public abstract VmElement map(VmXml vm);
    
    public abstract VmXml mapToXml(VmElement vm);
    
    public abstract List<DiskNode> mapDisks(List<DiskNodeXml> disks);
    
    public abstract List<DiskNodeXml> mapDisksToXml(List<DiskNode> disks);
    
    public abstract DiskNode map(DiskNodeXml disk);
    
    public abstract DiskNodeXml mapToXml(DiskNode disk);
    
    public abstract List<HistoryNode> mapHistories(List<HistoryNodeXml> histories);
    
    public abstract List<HistoryNodeXml> mapHistoriesToXml(List<HistoryNode> histories);
    
    public abstract HistoryNode map(HistoryNodeXml history);
    
    public abstract HistoryNodeXml mapToXml(HistoryNode history);
    
    public abstract List<NicNode> mapNics(List<NicNodeXml> nics);
    
    public abstract List<NicNodeXml> mapNicsToXml(List<NicNode> nics);
    
    public abstract NicNode map(NicNodeXml nic);
    
    public abstract NicNodeXml mapToXml(NicNode nic);
    
    public abstract List<PciNode> mapPcis(List<PciNodeXml> pcis);
    
    public abstract List<PciNodeXml> mapPcisToXml(List<PciNode> pcis);
    
    public abstract PciNode map(PciNodeXml pci);
    
    public abstract PciNodeXml mapToXml(PciNode pci);
    
}
