/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.pools;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

/**
 *
 * @author Gabriela Podolnikova
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class Network {
    
    @JacksonXmlProperty(localName = "ID")
    private String id;
    
    @JacksonXmlProperty(localName = "LEASES")
    private String leases;
    
    @JacksonXmlProperty(localName = "LEASES_USED")
    private String leases_used;

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the leases
     */
    public String getLeases() {
        return leases;
    }

    /**
     * @param leases the leases to set
     */
    public void setLeases(String leases) {
        this.leases = leases;
    }

    /**
     * @return the leases_used
     */
    public String getLeases_used() {
        return leases_used;
    }

    /**
     * @param leases_used the leases_used to set
     */
    public void setLeases_used(String leases_used) {
        this.leases_used = leases_used;
    }
}
