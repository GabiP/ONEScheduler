package cz.muni.fi.xml.resources.nodes;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

/**
 * This class represents the nic node retrived from xml.
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
