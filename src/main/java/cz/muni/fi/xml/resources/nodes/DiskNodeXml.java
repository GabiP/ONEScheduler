package cz.muni.fi.xml.resources.nodes;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

/**
 * This class represents the disk node retrived from xml.
 * @author Andras Urge
 */
@JacksonXmlRootElement(localName = "DISK")
public class DiskNodeXml {
        
    @JacksonXmlProperty(localName = "DATASTORE_ID")
    private Integer datastore_id;
    
    @JacksonXmlProperty(localName = "SIZE")
    private int size;
    
    @JacksonXmlProperty(localName = "TM_MAD")
    private String tmMadName;
    
    @JacksonXmlProperty(localName = "CLONE")
    private String clone;

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

    public String getTmMadName() {
        return tmMadName;
    }

    public void setTmMadName(String tmMadName) {
        this.tmMadName = tmMadName;
    }

    public String getClone() {
        return clone;
    }

    public void setClone(String clone) {
        this.clone = clone;
    }

    @Override
    public String toString() {
        return "DiskNodeXml{" + "size=" + size + ", datastore_id=" + datastore_id + '}';
    }
    
}
