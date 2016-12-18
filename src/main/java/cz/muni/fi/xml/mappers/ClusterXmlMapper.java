package cz.muni.fi.xml.mappers;

import cz.muni.fi.scheduler.elements.ClusterElement;
import cz.muni.fi.xml.resources.ClusterXml;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 *
 * @author Gabriela Podolnikova
 */
@Mapper
public abstract class ClusterXmlMapper {
    
    public abstract List<ClusterElement> map(List<ClusterXml> clusters);
    
    public abstract List<ClusterXml> mapToXml(List<ClusterElement> clusters);
    
    @Mapping(target = "reservedCpu", expression = "java((cluster.getReservedCpu() != null ? cluster.getReservedCpu()/100 : 0.00f))")
    @Mapping(target = "reservedMemory", expression = "java((cluster.getReservedMemory() != null ? cluster.getReservedMemory()/1024 : 0))")
    public abstract ClusterElement map(ClusterXml cluster);
    
    @Mapping(target = "reservedCpu", expression = "java((cluster.getReservedCpu())*100)")
    @Mapping(target = "reservedMemory", expression = "java((cluster.getReservedMemory())*1024)")
    public abstract ClusterXml mapToXml(ClusterElement cluster);
    
}
