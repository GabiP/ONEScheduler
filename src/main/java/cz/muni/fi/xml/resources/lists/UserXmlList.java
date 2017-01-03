package cz.muni.fi.xml.resources.lists;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import cz.muni.fi.xml.resources.UserXml;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * This class represents a list of users retrived from xml.
 * @author Andras Urge
 */
@JacksonXmlRootElement(localName = "USERPOOL")
public class UserXmlList {
    
    @JacksonXmlProperty(localName = "USER")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<UserXml> users;

    public List<UserXml> getUsers() {
        return users;
    }

    public void setUsers(List<UserXml> users) {
        this.users = users;
    }
    
    public void writeToFile(String filePath) throws IOException {
        File file = new File(filePath);         
        XmlMapper xmlMapper = new XmlMapper(); 
        xmlMapper.enable(SerializationFeature.INDENT_OUTPUT);
        xmlMapper.writeValue(file, this);
    }
}
