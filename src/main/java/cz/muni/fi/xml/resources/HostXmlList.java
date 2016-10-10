/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.xml.resources;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import java.util.List;

/**
 *
 * @author Andras Urge
 */
@JacksonXmlRootElement(localName = "HOSTPOOL")
public class HostXmlList {
    
    @JacksonXmlProperty(localName = "HOST")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<HostXml> hosts;

    public List<HostXml> getHosts() {
        return hosts;
    }

    public void setHosts(List<HostXml> hosts) {
        this.hosts = hosts;
    }
}
