package cz.muni.fi.xml.resources;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.util.List;

/**
 * This class represents the Datastore retrived from xml.
 * @author Gabriela Podolnikova
 */
public class DatastoreXml {
    
    @JacksonXmlProperty(localName = "ID")
    private Integer id;
    
    @JacksonXmlProperty(localName = "UID")
    private Integer uid;
    
    @JacksonXmlProperty(localName = "GID")
    private Integer gid;
    
    @JacksonXmlProperty(localName = "NAME")
    private String name;
    
    //0 - image, 1 - system, 2 - file
    @JacksonXmlProperty(localName = "TYPE")
    private Integer type;
    
    //0 - for monitoring
    @JacksonXmlProperty(localName = "STATE")
    private Integer state;
    
    @JacksonXmlProperty(localName = "TM_MAD")
    private String tmMadName;
    
    //in node permissions
    @JacksonXmlProperty(localName = "")
    private Integer owner_u;
    
    @JacksonXmlProperty(localName = "")
    private Integer owner_m;
    
    @JacksonXmlProperty(localName = "")
    private Integer owner_a;
    
    @JacksonXmlProperty(localName = "")
    private Integer group_u;
    
    @JacksonXmlProperty(localName = "")
    private Integer group_m;
    
    @JacksonXmlProperty(localName = "")
    private Integer group_a;

    @JacksonXmlProperty(localName = "")
    private Integer other_u;
    
    @JacksonXmlProperty(localName = "")
    private Integer other_m;
    
    @JacksonXmlProperty(localName = "")
    private Integer other_a;
    
    @JacksonXmlProperty(localName = "CLUSTERS")
    private List<Integer> clusters;
    
    @JacksonXmlProperty(localName = "TOTAL_MB")
    private Integer total_mb;
    
    @JacksonXmlProperty(localName = "FREE_MB")
    private Integer free_mb;
    
    @JacksonXmlProperty(localName = "USED_MB")
    private Integer used_mb;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public Integer getGid() {
        return gid;
    }

    public void setGid(Integer gid) {
        this.gid = gid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getOwner_u() {
        return owner_u;
    }

    public void setOwner_u(Integer owner_u) {
        this.owner_u = owner_u;
    }

    public Integer getOwner_m() {
        return owner_m;
    }

    public void setOwner_m(Integer owner_m) {
        this.owner_m = owner_m;
    }

    public Integer getOwner_a() {
        return owner_a;
    }

    public void setOwner_a(Integer owner_a) {
        this.owner_a = owner_a;
    }

    public Integer getGroup_u() {
        return group_u;
    }

    public void setGroup_u(Integer group_u) {
        this.group_u = group_u;
    }

    public Integer getGroup_m() {
        return group_m;
    }

    public void setGroup_m(Integer group_m) {
        this.group_m = group_m;
    }

    public Integer getGroup_a() {
        return group_a;
    }

    public void setGroup_a(Integer group_a) {
        this.group_a = group_a;
    }

    public Integer getOther_u() {
        return other_u;
    }

    public void setOther_u(Integer other_u) {
        this.other_u = other_u;
    }

    public Integer getOther_m() {
        return other_m;
    }

    public void setOther_m(Integer other_m) {
        this.other_m = other_m;
    }

    public Integer getOther_a() {
        return other_a;
    }

    public void setOther_a(Integer other_a) {
        this.other_a = other_a;
    }

    public List<Integer> getClusters() {
        return clusters;
    }

    public void setClusters(List<Integer> clusters) {
        this.clusters = clusters;
    }

    public Integer getTotal_mb() {
        return total_mb;
    }

    public void setTotal_mb(Integer total_mb) {
        this.total_mb = total_mb;
    }

    public Integer getFree_mb() {
        return free_mb;
    }

    public void setFree_mb(Integer free_mb) {
        this.free_mb = free_mb;
    }

    public Integer getUsed_mb() {
        return used_mb;
    }

    public void setUsed_mb(Integer used_mb) {
        this.used_mb = used_mb;
    }

    @Override
    public String toString() {
        return "DatastoreXml{" + "id=" + id + ", uid=" + uid + ", gid=" + gid + ", name=" + name + ", type=" + type + ", clusters=" + clusters + ", total_mb=" + total_mb + ", free_mb=" + free_mb + ", used_mb=" + used_mb + '}';
    }

    /**
     * @return the state
     */
    public Integer getState() {
        return state;
    }

    /**
     * @param state the state to set
     */
    public void setState(Integer state) {
        this.state = state;
    }

    /**
     * @return the tmMadName
     */
    public String getTmMadName() {
        return tmMadName;
    }

    /**
     * @param tmMadName the tmMadName to set
     */
    public void setTmMadName(String tmMadName) {
        this.tmMadName = tmMadName;
    }
}
