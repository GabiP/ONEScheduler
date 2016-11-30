/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.xml.resources.lists;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import cz.muni.fi.xml.resources.VmXml;
import java.util.List;

/**
 *
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
}
