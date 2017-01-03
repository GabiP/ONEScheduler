package cz.muni.fi.xml.mappers;

import cz.muni.fi.scheduler.elements.GroupElement;
import cz.muni.fi.xml.resources.GroupXml;
import java.util.List;
import org.mapstruct.Mapper;

/**
 * Maps GroupXml to GroupElement and vice-versa.
 * 
 * @author Gabriela Podolnikova
 */
@Mapper
public abstract class GroupXmlMapper {

    public abstract List<GroupElement> map(List<GroupXml> groups);
    
    public abstract List<GroupXml> mapToXml(List<GroupElement> groups);
    
    public abstract GroupElement map(GroupXml group);
    
    public abstract GroupXml mapToXml(GroupElement group);
    
}
