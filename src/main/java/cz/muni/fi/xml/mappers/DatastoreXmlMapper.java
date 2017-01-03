package cz.muni.fi.xml.mappers;

import cz.muni.fi.scheduler.elements.DatastoreElement;
import cz.muni.fi.xml.resources.DatastoreXml;
import java.util.List;
import org.mapstruct.Mapper;

/**
 * Maps DatastoreXml to DatastoreElement and vice-versa.
 * 
 * @author Gabriela Podolnikova
 */
@Mapper
public abstract class DatastoreXmlMapper {

    public abstract List<DatastoreElement> map(List<DatastoreXml> datastores);
    
    public abstract List<DatastoreXml> mapToXml(List<DatastoreElement> datastores);
    
    public abstract DatastoreElement map(DatastoreXml datastore);
    
    public abstract DatastoreXml mapToXml(DatastoreElement datastore);

}
