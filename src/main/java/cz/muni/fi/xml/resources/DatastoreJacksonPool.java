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
 * @author Gabriela Podolnikova
 */
@JacksonXmlRootElement(localName = "DATASTORE_POOL")
public class DatastoreJacksonPool {
    
    @JacksonXmlProperty(localName = "DATASTORE")
    @JacksonXmlElementWrapper(useWrapping = false)
    private DatastoreXml[] datastores;

    public DatastoreXml[] getDatastores() {
        return datastores;
    }

    public void setDatastores(DatastoreXml[] datastores) {
        this.datastores = datastores;
    }

    @Override
    public String toString() {
        return "DatastoreJacksonPool{" + "datastores=" + Arrays.toString(datastores) + '}';
    }
    
}
