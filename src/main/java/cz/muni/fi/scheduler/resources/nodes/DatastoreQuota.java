package cz.muni.fi.scheduler.resources.nodes;

import org.opennebula.client.PoolElement;

/**
 *
 * @author Gabriela Podolnikova
 */
public class DatastoreQuota extends AbstractNode {
    
    private Integer id;
    
    private Integer images;
    
    private Integer imagesUsed;
    
    private Integer size;
    
    private Integer sizeUsed;
    
    @Override
    public void load(PoolElement user, String xpathExpr) {
        id = Integer.parseInt(user.xpath(xpathExpr + "/ID"));
        images = Integer.parseInt(user.xpath(xpathExpr + "/IMAGES"));
        imagesUsed = Integer.parseInt(user.xpath(xpathExpr + "/IMAGES_USED"));
        size = Integer.parseInt(user.xpath(xpathExpr + "/SIZE"));
        sizeUsed = Integer.parseInt(user.xpath(xpathExpr + "/SIZE_USED"));
    }
    
    public boolean isEmpty() {
        return (images == null && size == null);
    }
      
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

    @Override
    public String toString() {
        return "DatastoreQuota{" + "id=" + id + ", images=" + images + ", imagesUsed=" + imagesUsed + ", size=" + size + ", sizeUsed=" + sizeUsed + '}';
    }
}
