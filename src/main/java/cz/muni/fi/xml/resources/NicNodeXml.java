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
 * @author Andras Urge
 */
@JacksonXmlRootElement(localName = "NIC")
public class NicNodeXml {
    
    @JacksonXmlProperty(localName = "NETWORK_ID")    
    private Integer networkId;

    public Integer getNetworkId() {
        return networkId;
    }

    public void setNetworkId(Integer networkId) {
        this.networkId = networkId;
    }

    @Override
    public String toString() {
        return "NicNodeXml{" + "networkId=" + networkId + '}';
    }
}
