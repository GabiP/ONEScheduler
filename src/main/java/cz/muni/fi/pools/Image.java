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
public class Image {
    
    @JacksonXmlProperty(localName = "ID")
    private String id;
    
    @JacksonXmlProperty(localName = "RVMS")
    private String rvms;
    
    @JacksonXmlProperty(localName = "RVMS_USED")
    private String rvms_used;

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
     * @return the rvms
     */
    public String getRvms() {
        return rvms;
    }

    /**
     * @param rvms the rvms to set
     */
    public void setRvms(String rvms) {
        this.rvms = rvms;
    }

    /**
     * @return the rvms_used
     */
    public String getRvms_used() {
        return rvms_used;
    }

    /**
     * @param rvms_used the rvms_used to set
     */
    public void setRvms_used(String rvms_used) {
        this.rvms_used = rvms_used;
    }
    
}
