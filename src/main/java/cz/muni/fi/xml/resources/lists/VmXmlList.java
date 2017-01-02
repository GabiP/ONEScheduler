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
import cz.muni.fi.xml.resources.VmXml;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * This class represents a list of vms retrived from xml.
 * @author Andras Urge
 */
@JacksonXmlRootElement(localName = "VMPOOL")
public class VmXmlList {
    
    @JacksonXmlProperty(localName = "VM")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<VmXml> vms;

    public List<VmXml> getVms() {
        return vms;
    }

    public void setVms(List<VmXml> vms) {
        this.vms = vms;
    }    
    
    public void writeToFile(String filePath) throws IOException {
        File file = new File(filePath);         
        XmlMapper xmlMapper = new XmlMapper(); 
        xmlMapper.enable(SerializationFeature.INDENT_OUTPUT);
        xmlMapper.writeValue(file, this);
    }
}
