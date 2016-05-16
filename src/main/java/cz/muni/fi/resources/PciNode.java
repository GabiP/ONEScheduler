/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.resources;

import org.opennebula.client.PoolElement;

/**
 *
 * @author Gabriela Podolnikova
 */
public class PciNode extends NodeElement {

    private static final String XPATH_EXPR = "/HOST/HOST_SHARE/PCI_DEVICES/PCI";
    
    private String pci_class;
    private String device;
    private String vendor;
    
    @Override
    void load(PoolElement host, int i) {
        pci_class = host.xpath(XPATH_EXPR + "["+i+"]" + "/CLASS");
        device = host.xpath(XPATH_EXPR + "["+i+"]" + "/DEVICE");
        vendor = host.xpath(XPATH_EXPR + "["+i+"]" + "/VENDOR");
    }

    @Override
    String getXpathExpr() {
        return XPATH_EXPR;
    }

    /**
     * @param pci_class the pci_class to set
     */
    public void setPci_class(String pci_class) {
        this.pci_class = pci_class;
    }

    /**
     * @param device the device to set
     */
    public void setDevice(String device) {
        this.device = device;
    }

    /**
     * @param vendor the vendor to set
     */
    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    /**
     * @return the pci_class
     */
    public String getPci_class() {
        return pci_class;
    }

    /**
     * @return the device
     */
    public String getDevice() {
        return device;
    }

    /**
     * @return the vendor
     */
    public String getVendor() {
        return vendor;
    }

    
    
}
