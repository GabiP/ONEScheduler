package cz.muni.fi.xml.resources.lists;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import cz.muni.fi.xml.resources.DatastoreXml;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * This class represents a list of datastores retrived from xml.
 * @author Andras Urge
 */
@JacksonXmlRootElement(localName = "DATASTOREPOOL")
public class DatastoreXmlList {
    
    @JacksonXmlProperty(localName = "DATASTORE")
    @JacksonXmlElementWrapper(useWrapping = false)    
    private List<DatastoreXml> datastores;      

    public List<DatastoreXml> getDatastores() {
        return datastores;
    }

    public void setDatastores(List<DatastoreXml> datastores) {
        this.datastores = datastores;
    }
    
    public void writeToFile(String filePath) throws IOException {
        File file = new File(filePath);         
        XmlMapper xmlMapper = new XmlMapper(); 
        xmlMapper.enable(SerializationFeature.INDENT_OUTPUT);
        xmlMapper.writeValue(file, this);
    }
}
