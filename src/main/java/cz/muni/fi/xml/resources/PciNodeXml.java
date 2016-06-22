/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.xml.resources;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

/**
 *
 * @author gabi
 */
@JacksonXmlRootElement(localName = "PCI")
public class PciNodeXml {
    
    @JacksonXmlProperty(localName = "CLASS")
    private String pci_class;
    
    @JacksonXmlProperty(localName = "DEVICE")
    private String device;
    
    @JacksonXmlProperty(localName = "VENDOR")
    private String vendor;

    public String getPci_class() {
        return pci_class;
    }

    public void setPci_class(String pci_class) {
        this.pci_class = pci_class;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    @Override
    public String toString() {
        return "PciNodeXml{" + "pci_class=" + pci_class + ", device=" + device + ", vendor=" + vendor + '}';
    }
    
}
