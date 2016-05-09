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
    
    private String device_name;
    
    @Override
    void load(PoolElement host, int i) {
        device_name = host.xpath(XPATH_EXPR + "["+i+"]" + "/DEVICE_NAME");
    }

    @Override
    String getXpathExpr() {
        return XPATH_EXPR;
    }

    /**
     * @return the device_name
     */
    public String getDevice_name() {
        return device_name;
    }

    /**
     * @param device_name the device_name to set
     */
    public void setDevice_name(String device_name) {
        this.device_name = device_name;
    }
    
}
