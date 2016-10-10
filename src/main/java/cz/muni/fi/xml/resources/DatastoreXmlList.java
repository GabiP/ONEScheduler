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
@JacksonXmlRootElement(localName = "DATASTOREPOOL")
public class DatastoreXmlList {
    
    @JacksonXmlProperty(localName = "DATASTORE")
    @JacksonXmlElementWrapper(useWrapping = false)    
    private List<DatastoreXml> datastores;      

    public List<DatastoreXml> getDatastores() {
        return datastores;
    }

    public void setDatastores(List<DatastoreXml> datastores) {
        this.datastores = datastores;
    }
}
