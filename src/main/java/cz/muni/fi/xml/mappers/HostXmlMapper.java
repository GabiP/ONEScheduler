package cz.muni.fi.xml.mappers;

import cz.muni.fi.scheduler.elements.HostElement;
import cz.muni.fi.scheduler.elements.nodes.DatastoreNode;
import cz.muni.fi.scheduler.elements.nodes.PciNode;
import cz.muni.fi.xml.resources.nodes.DatastoreNodeXml;
import cz.muni.fi.xml.resources.HostXml;
import cz.muni.fi.xml.resources.nodes.PciNodeXml;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 *
 * @author Gabriela Podolnikova
 */
@Mapper
public abstract class HostXmlMapper {
    
    public abstract List<HostElement> map(List<HostXml> hosts);
    
    public abstract List<HostXml> mapToXml(List<HostElement> hosts);
    
    @Mapping(target = "mem_usage", expression = "java(host.getMem_usage()/1024)")
    @Mapping(target = "cpu_usage", expression = "java(host.getCpu_usage()/100)")
    @Mapping(target = "max_mem", expression = "java(host.getMax_mem()/1024)")
    @Mapping(target = "max_cpu", expression = "java(host.getMax_cpu()/100)")
    @Mapping(target = "free_mem", expression = "java(host.getFree_mem()/1024)")
    @Mapping(target = "free_cpu", expression = "java(host.getFree_cpu()/100)")
    @Mapping(target = "used_mem", expression = "java(host.getUsed_mem()/1024)")
    @Mapping(target = "used_cpu", expression = "java(host.getUsed_cpu()/100)")
    @Mapping(target = "reservedCpu", expression = "java((host.getReservedCpu() != null ? host.getReservedCpu()/100 : 0.00f))")
    @Mapping(target = "reservedMemory", expression = "java((host.getReservedMemory() != null ? host.getReservedMemory()/1024 : 0))")
    public abstract HostElement map(HostXml host);
    
    @Mapping(target = "mem_usage", expression = "java(host.getMem_usage()*1024)")
    @Mapping(target = "cpu_usage", expression = "java(host.getCpu_usage()*100)")
    @Mapping(target = "max_mem", expression = "java(host.getMax_mem()*1024)")
    @Mapping(target = "max_cpu", expression = "java(host.getMax_cpu()*100)")
    @Mapping(target = "free_mem", expression = "java(host.getFree_mem()*1024)")
    @Mapping(target = "free_cpu", expression = "java(host.getFree_cpu()*100)")
    @Mapping(target = "used_mem", expression = "java(host.getUsed_mem()*1024)")
    @Mapping(target = "used_cpu", expression = "java(host.getUsed_cpu()*100)")
    @Mapping(target = "reservedCpu", expression = "java((host.getReservedCpu())*100)")
    @Mapping(target = "reservedMemory", expression = "java((host.getReservedMemory())*1024)")
    public abstract HostXml mapToXml(HostElement host);

    public abstract List<PciNode> mapPcis(List<PciNodeXml> pcis);
    
    public abstract List<PciNodeXml> mapPcisToXml(List<PciNode> pcis);
            
    public abstract PciNode map(PciNodeXml pci);
    
    public abstract PciNodeXml mapToXml(PciNode pci);
    
    public abstract List<DatastoreNode> mapDatastores(List<DatastoreNodeXml> datastores);
    
    public abstract List<DatastoreNodeXml> mapDatastoresToXml(List<DatastoreNode> datastores);
    
    public abstract DatastoreNode map(DatastoreNodeXml ds);
    
    public abstract DatastoreNodeXml mapToXml(DatastoreNode ds);
    
}
