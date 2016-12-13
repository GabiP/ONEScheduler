/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.xml.resources.lists;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import cz.muni.fi.xml.resources.HostXml;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author Andras Urge
 */
@JacksonXmlRootElement(localName = "HOSTPOOL")
public class HostXmlList {
    
    @JacksonXmlProperty(localName = "HOST")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<HostXml> hosts;

    public List<HostXml> getHosts() {
        return hosts;
    }

    public void setHosts(List<HostXml> hosts) {
        this.hosts = hosts;
    }
    
    public void writeToFile(String filePath) throws IOException {
        File file = new File(filePath);         
        XmlMapper xmlMapper = new XmlMapper(); 
        xmlMapper.enable(SerializationFeature.INDENT_OUTPUT);
        xmlMapper.writeValue(file, this);
    }
}
