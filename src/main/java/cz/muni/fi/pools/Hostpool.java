package cz.muni.fi.pools;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.Arrays;

/**
 * This class represents an OpenNebula Vm pool.
 *
 * @author Gabriela Podolnikova
 */
@JacksonXmlRootElement(localName = "HOSTPOOL")
public class Hostpool {

    @JacksonXmlProperty(localName = "HOST")
    @JacksonXmlElementWrapper(useWrapping = false)
    private Host[] hosts;

    public Host[] getHosts() {
        return hosts;
    }

    public void setHosts(Host[] hosts) {
        this.hosts = hosts;
    }

    @Override
    public String toString() {
        return "Hostpool{" +
                "hosts=" + Arrays.toString(hosts) +
                '}';
    }
}
