package cz.muni.fi.xml.resources.lists;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import cz.muni.fi.xml.resources.ClusterXml;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * This class represents a list of clusters retrived from xml.
 * @author Andras Urge
 */
@JacksonXmlRootElement(localName = "CLUSTERPOOL")
public class ClusterXmlList {
    
    @JacksonXmlProperty(localName = "CLUSTER")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<ClusterXml> clusters;

    public List<ClusterXml> getClusters() {
        return clusters;
    }

    public void setClusters(List<ClusterXml> clusters) {
        this.clusters = clusters;
    }    
    
    public void writeToFile(String filePath) throws IOException {
        File file = new File(filePath);         
        XmlMapper xmlMapper = new XmlMapper(); 
        xmlMapper.enable(SerializationFeature.INDENT_OUTPUT);
        xmlMapper.writeValue(file, this);
    }
}
