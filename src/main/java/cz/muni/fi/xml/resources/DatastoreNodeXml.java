/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.xml.resources;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

/**
 *
 * @author gabi
 */
@JacksonXmlRootElement(localName = "DATASTORE")
public class DatastoreNodeXml {
    
    @JacksonXmlProperty(localName = "ID")
    private int id_ds;
    
    @JacksonXmlProperty(localName = "FREE_MB")
    private int free_mb;
    
    @JacksonXmlProperty(localName = "TOTAL_MB")
    private int total_mb;
    
    @JacksonXmlProperty(localName = "USED_CPU")
    private int used_mb;

    public int getId_ds() {
        return id_ds;
    }

    public void setId_ds(int id_ds) {
        this.id_ds = id_ds;
    }

    public int getFree_mb() {
        return free_mb;
    }

    public void setFree_mb(int free_mb) {
        this.free_mb = free_mb;
    }

    public int getTotal_mb() {
        return total_mb;
    }

    public void setTotal_mb(int total_mb) {
        this.total_mb = total_mb;
    }

    public int getUsed_mb() {
        return used_mb;
    }

    public void setUsed_mb(int used_mb) {
        this.used_mb = used_mb;
    }

    @Override
    public String toString() {
        return "DatastoreNodeXml{" + "id_ds=" + id_ds + ", free_mb=" + free_mb + ", total_mb=" + total_mb + ", used_mb=" + used_mb + '}';
    }
}
