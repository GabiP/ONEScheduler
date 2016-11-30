package cz.muni.fi.scheduler.resources.nodes;

import org.opennebula.client.PoolElement;

/**
 * This class represents a PciNode.
 * A virtual machine or a host can have multiple pci nodes.
 * Loads the data from xml by using OpenNebula's API.
 * 
 * @author Gabriela Podolnikova
 */
public class PciNode extends AbstractNode {
   
    private String pci_class;
    private String device;
    private String vendor;
    
    @Override
    public void load(PoolElement host, String xpathExpr) {
        pci_class = host.xpath(xpathExpr + "/CLASS");
        device = host.xpath(xpathExpr + "/DEVICE");
        vendor = host.xpath(xpathExpr + "/VENDOR");
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

    @Override
    public String toString() {
        return "PciNode{" + "pci_class=" + pci_class + ", device=" + device + ", vendor=" + vendor + '}';
    }
}
