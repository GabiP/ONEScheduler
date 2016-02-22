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
 * This class represents OpenNebula User Datastore.
 * 
 * @author Gabriela Podolnikova
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class Datastore {
    
    @JacksonXmlProperty(localName = "ID")
    private String id;
    
    @JacksonXmlProperty(localName = "IMAGES")
    private String images;
    
    @JacksonXmlProperty(localName = "IMAGES_USED")
    private String images_used;
    
    @JacksonXmlProperty(localName = "SIZE")
    private String size;
    
    @JacksonXmlProperty(localName = "SIZE_USED")
    private String size_used;

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
     * @return the images
     */
    public String getImages() {
        return images;
    }

    /**
     * @param images the images to set
     */
    public void setImages(String images) {
        this.images = images;
    }

    /**
     * @return the images_used
     */
    public String getImages_used() {
        return images_used;
    }

    /**
     * @param images_used the images_used to set
     */
    public void setImages_used(String images_used) {
        this.images_used = images_used;
    }

    /**
     * @return the size
     */
    public String getSize() {
        return size;
    }

    /**
     * @param size the size to set
     */
    public void setSize(String size) {
        this.size = size;
    }

    /**
     * @return the size_used
     */
    public String getSize_used() {
        return size_used;
    }

    /**
     * @param size_used the size_used to set
     */
    public void setSize_used(String size_used) {
        this.size_used = size_used;
    }
}
