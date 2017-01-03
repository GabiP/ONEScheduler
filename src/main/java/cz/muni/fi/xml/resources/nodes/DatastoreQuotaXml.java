package cz.muni.fi.xml.resources.nodes;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

/**
 * This class represents a Datastore quota node retrieved from xml.
 * @author Gabriela Podolnikova
 */
@JacksonXmlRootElement(localName = "DATASTORE")
public class DatastoreQuotaXml {
    
    @JacksonXmlProperty(localName = "ID")
    private Integer id;
    
    @JacksonXmlProperty(localName = "IMAGES")
    private Integer images;
    
    @JacksonXmlProperty(localName = "IMAGES_USED")
    private Integer imagesUsed;
    
    @JacksonXmlProperty(localName = "SIZE")
    private Integer size;
    
    @JacksonXmlProperty(localName = "SIZE_USED")
    private Integer sizeUsed;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getImages() {
        return images;
    }

    public void setImages(Integer images) {
        this.images = images;
    }

    public Integer getImagesUsed() {
        return imagesUsed;
    }

    public void setImagesUsed(Integer imagesUsed) {
        this.imagesUsed = imagesUsed;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Integer getSizeUsed() {
        return sizeUsed;
    }

    public void setSizeUsed(Integer sizeUsed) {
        this.sizeUsed = sizeUsed;
    }
    
    
}
