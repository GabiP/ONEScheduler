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
@JacksonXmlRootElement(localName = "DISK")
public class DiskNodeXml {
    
    @JacksonXmlProperty(localName = "SIZE")
    private int size;
    
    @JacksonXmlProperty(localName = "DATASTORE_ID")
    private Integer datastore_id;

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public Integer getDatastore_id() {
        return datastore_id;
    }

    public void setDatastore_id(Integer datastore_id) {
        this.datastore_id = datastore_id;
    }

    @Override
    public String toString() {
        return "DiskNodeXml{" + "size=" + size + ", datastore_id=" + datastore_id + '}';
    }
    
}
