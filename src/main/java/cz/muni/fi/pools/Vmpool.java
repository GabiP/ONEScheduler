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
@JacksonXmlRootElement(localName = "VMPOOL")
public class Vmpool {
    @JacksonXmlProperty(localName = "VM")
    @JacksonXmlElementWrapper(useWrapping = false)
    private Vm[] vms;

    /**
     * @return the vms
     */
    public Vm[] getVms() {
        return vms;
    }

    /**
     * @param vms the vms to set
     */
    public void setVms(Vm[] vms) {
        this.vms = vms;
    }
    
    @Override
    public String toString() {
        return "VMpool{" +
                "vms=" + Arrays.toString(vms) +
                '}';
    }
}
