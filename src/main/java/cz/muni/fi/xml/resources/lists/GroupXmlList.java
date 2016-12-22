package cz.muni.fi.xml.resources.lists;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import cz.muni.fi.xml.resources.GroupXml;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author Gabriela Podolnikova
 */
@JacksonXmlRootElement(localName = "GROUPPOOL")
public class GroupXmlList {

    @JacksonXmlProperty(localName = "GROUP")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<GroupXml> groups;

    public List<GroupXml> getGroups() {
        return groups;
    }

    public void setGroups(List<GroupXml> groups) {
        this.groups = groups;
    }
    
    public void writeToFile(String filePath) throws IOException {
        File file = new File(filePath);         
        XmlMapper xmlMapper = new XmlMapper(); 
        xmlMapper.enable(SerializationFeature.INDENT_OUTPUT);
        xmlMapper.writeValue(file, this);
    }
    
}
