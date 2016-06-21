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
 * @author gabi
 */
@JacksonXmlRootElement(localName = "HOSTPOOL")
public class HostJacksonPool {
    @JacksonXmlProperty(localName = "HOST")
    @JacksonXmlElementWrapper(useWrapping = false)
    private HostXml[] hosts;

    public HostXml[] getHosts() {
        return hosts;
    }

    public void setHosts(HostXml[] hosts) {
        this.hosts = hosts;
    }

    @Override
    public String toString() {
        return "Hostpool{" +
                "hosts=" + Arrays.toString(hosts) +
                '}';
    }
}
