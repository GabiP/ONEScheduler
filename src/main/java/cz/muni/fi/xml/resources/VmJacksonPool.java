/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.xml.resources;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import java.util.Arrays;

/**
 *
 * @author Andras Urge
 */
@JacksonXmlRootElement(localName = "VMPOOL")
public class VmJacksonPool {
    @JacksonXmlProperty(localName = "VM")
    @JacksonXmlElementWrapper(useWrapping = false)
    private VmXml[] vms;

    public VmXml[] getVms() {
        return vms;
    }

    public void setVms(VmXml[] vms) {
        this.vms = vms;
    }
    
    @Override
    public String toString() {
        return "Vmpool{" +
                "Vms=" + Arrays.toString(vms) +
                '}';
    }
}
