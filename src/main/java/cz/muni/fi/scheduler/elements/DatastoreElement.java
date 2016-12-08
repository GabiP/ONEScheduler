package cz.muni.fi.scheduler.elements;

import cz.muni.fi.one.oned.TmMadConfiguration;
import java.util.List;
import java.util.Objects;

/**
 * This class represents a Datastore.
 * A Datastore is any storage medium used to store disk images for VMs.
 * There are three types of Datastores:
 * - System, to hold images for running VMs.
 * - Images, stores the disk images repository. Disk images are moved, or cloned to/from the System datastore when the VMs are deployed or shutdown; or when disks are attached or snapshoted.
 * - Files, This is a special datastore used to store plain files and not disk images. The plain files can be used as kernels, ramdisks or context files.
 * For more information see: http://docs.opennebula.org/4.12/administration/storage/sm.html
 * 
 * @author Gabriela Podolnikova
 */
public class DatastoreElement {
    
    private Integer id;  
    private Integer uid;  
    private Integer gid;
    
    private String name;
    
    //0 - image, 1 - system, 2 - file
    private Integer type;
    
    //0 - for monitoring
    private Integer state;
    
    private String tmMadName;
    
    private Integer owner_u;
    private Integer owner_m;
    private Integer owner_a;

    private Integer group_u;
    private Integer group_m;
    private Integer group_a;

    private Integer other_u;
    private Integer other_m;
    private Integer other_a;
    
    private List<Integer> clusters;
    
    private Integer total_mb;
    private Integer free_mb;
    private Integer used_mb;    
    
    public Boolean isShared() {
        String shared = TmMadConfiguration.getSharedInfo(tmMadName);
        return "YES".equals(shared);
    }
    
    public Boolean hasCloneTargetSelf() {
        String cloneTarget = TmMadConfiguration.getCloneTargetInfo(tmMadName);
        return "SELF".equals(cloneTarget);
    }
    
    public Boolean hasLnTargetSelf() {
        String cloneTarget = TmMadConfiguration.getLnTargetInfo(tmMadName);
        return "SELF".equals(cloneTarget);
    }
    
    public Boolean isMonitored() {
        return (Objects.equals(0, this.state));
    }
    
    public Boolean isSystem() {
        return (Objects.equals(1, this.type));
    }
    
    public Boolean isImage() {
        return (Objects.equals(0, this.type));
    }
    
    /**
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return the uid
     */
    public Integer getUid() {
        return uid;
    }

    /**
     * @param uid the uid to set
     */
    public void setUid(Integer uid) {
        this.uid = uid;
    }

    /**
     * @return the gid
     */
    public Integer getGid() {
        return gid;
    }

    /**
     * @param gid the gid to set
     */
    public void setGid(Integer gid) {
        this.gid = gid;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the owner_u
     */
    public Integer getOwner_u() {
        return owner_u;
    }

    /**
     * @param owner_u the owner_u to set
     */
    public void setOwner_u(Integer owner_u) {
        this.owner_u = owner_u;
    }

    /**
     * @return the owner_m
     */
    public Integer getOwner_m() {
        return owner_m;
    }

    /**
     * @param owner_m the owner_m to set
     */
    public void setOwner_m(Integer owner_m) {
        this.owner_m = owner_m;
    }

    /**
     * @return the owner_a
     */
    public Integer getOwner_a() {
        return owner_a;
    }

    /**
     * @param owner_a the owner_a to set
     */
    public void setOwner_a(Integer owner_a) {
        this.owner_a = owner_a;
    }

    /**
     * @return the group_u
     */
    public Integer getGroup_u() {
        return group_u;
    }

    /**
     * @param group_u the group_u to set
     */
    public void setGroup_u(Integer group_u) {
        this.group_u = group_u;
    }

    /**
     * @return the group_m
     */
    public Integer getGroup_m() {
        return group_m;
    }

    /**
     * @param group_m the group_m to set
     */
    public void setGroup_m(Integer group_m) {
        this.group_m = group_m;
    }

    /**
     * @return the group_a
     */
    public Integer getGroup_a() {
        return group_a;
    }

    /**
     * @param group_a the group_a to set
     */
    public void setGroup_a(Integer group_a) {
        this.group_a = group_a;
    }

    /**
     * @return the other_u
     */
    public Integer getOther_u() {
        return other_u;
    }

    /**
     * @param other_u the other_u to set
     */
    public void setOther_u(Integer other_u) {
        this.other_u = other_u;
    }

    /**
     * @return the other_m
     */
    public Integer getOther_m() {
        return other_m;
    }

    /**
     * @param other_m the other_m to set
     */
    public void setOther_m(Integer other_m) {
        this.other_m = other_m;
    }

    /**
     * @return the other_a
     */
    public Integer getOther_a() {
        return other_a;
    }

    /**
     * @param other_a the other_a to set
     */
    public void setOther_a(Integer other_a) {
        this.other_a = other_a;
    }

    /**
     * @return the clusters
     */
    public List<Integer> getClusters() {
        return clusters;
    }

    /**
     * @param cluster_id the cluster_id to set
     */
    public void setClusters(List<Integer> clusters) {
        this.clusters = clusters;
    }

    /**
     * @return the total_mb
     */
    public Integer getTotal_mb() {
        return total_mb;
    }

    /**
     * @param total_mb the total_mb to set
     */
    public void setTotal_mb(Integer total_mb) {
        this.total_mb = total_mb;
    }

    /**
     * @return the free_mb
     */
    public Integer getFree_mb() {
        return free_mb;
    }

    /**
     * @param free_mb the free_mb to set
     */
    public void setFree_mb(Integer free_mb) {
        this.free_mb = free_mb;
    }

    /**
     * @return the used_mb
     */
    public Integer getUsed_mb() {
        return used_mb;
    }

    /**
     * @param used_mb the used_mb to set
     */
    public void setUsed_mb(Integer used_mb) {
        this.used_mb = used_mb;
    }

    /**
     * @return the type
     */
    public Integer getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(Integer type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "DatastoreElement{" + "id=" + id + ", uid=" + uid + ", gid=" + gid + ", name=" + name + ", type=" + type + ", clusters=" + clusters + ", total_mb=" + total_mb + ", free_mb=" + free_mb + ", used_mb=" + used_mb + '}';
    }
    
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 47 * hash + Objects.hashCode(this.getId());
        hash = 47 * hash + Objects.hashCode(this.getName());
        hash = 47 * hash + Objects.hashCode(this.getType());
        hash = 47 * hash + Objects.hashCode(this.getClusters());
        return hash;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof DatastoreElement)) {
            return false;
        }
        final DatastoreElement other = (DatastoreElement) obj;
        if (!Objects.equals(this.getId(), other.getId())) {
            return false;
        }
        if (!Objects.equals(this.getName(), other.getName())) {
            return false;
        }
        if (!Objects.equals(this.getType(), other.getType())) {
            return false;
        }
        if (!Objects.equals(this.getClusters(), other.getClusters())) {
            return false;
        }
        return true;
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
