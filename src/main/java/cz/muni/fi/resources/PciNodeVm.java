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
public class PciNodeVm extends NodeElement {
    
     private static final String XPATH_EXPR = "/VM/TEMPLATE/PCI";
    
    private String pci_class;
    private String device;
    private String vendor;

    @Override
    void load(PoolElement vm, int i) {
        pci_class = vm.xpath(XPATH_EXPR + "["+i+"]" + "/CLASS");
        device = vm.xpath(XPATH_EXPR + "["+i+"]" + "/DEVICE");
        vendor = vm.xpath(XPATH_EXPR + "["+i+"]" + "/VENDOR");
        System.out.println("Inside load PciNodeVm-->pci_class: " + pci_class + " device: " + device  + " vendor: " + vendor);
    }

    @Override
    String getXpathExpr() {
        return XPATH_EXPR;
    }

    /**
     * @return the pci_class
     */
    public String getPci_class() {
        return pci_class;
    }

    /**
     * @param pci_class the pci_class to set
     */
    public void setPci_class(String pci_class) {
        this.pci_class = pci_class;
    }

    /**
     * @return the device
     */
    public String getDevice() {
        return device;
    }

    /**
     * @param device the device to set
     */
    public void setDevice(String device) {
        this.device = device;
    }

    /**
     * @return the vendor
     */
    public String getVendor() {
        return vendor;
    }

    /**
     * @param vendor the vendor to set
     */
    public void setVendor(String vendor) {
        this.vendor = vendor;
    }
    
}
