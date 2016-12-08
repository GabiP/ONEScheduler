package cz.muni.fi.scheduler.elements;

import cz.muni.fi.scheduler.elements.nodes.DatastoreQuota;
import cz.muni.fi.scheduler.elements.nodes.VmQuota;
import java.util.List;
import java.util.Objects;

/**
 * This class represents a User.
 * Each user has a unique ID, and belongs to a group.
 * 
 * @author Gabriela Podolnikova
 */
public class UserElement {
    
    private Integer id;
    
    private Integer gid;
    
    private List<Integer> groups;
    
    private List<DatastoreQuota> datastoreQuota;
    
    private VmQuota vmQuota;
        
    @Override
    public String toString() {
        return "User {" +
                "id='" + id + '\'' +
                '}';
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
     * @return the groups
     */
    public List<Integer> getGroups() {
        return groups;
    }

    public void setGroups(List<Integer> groups) {
        this.groups = groups;
    }

    public List<DatastoreQuota> getDatastoreQuota() {
        return datastoreQuota;
    }

    public void setDatastoreQuota(List<DatastoreQuota> datastoreQuota) {
        this.datastoreQuota = datastoreQuota;
    }

    public VmQuota getVmQuota() {
        return vmQuota;
    }

    public void setVmQuota(VmQuota vmQuota) {
        this.vmQuota = vmQuota;
    }
    
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 47 * hash + Objects.hashCode(this.getId());
        hash = 47 * hash + Objects.hashCode(this.getGid());
        return hash;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof UserElement)) {
            return false;
        }
        final UserElement other = (UserElement) obj;
        if (!Objects.equals(this.getId(), other.getId())) {
            return false;
        }
        if (!Objects.equals(this.getGid(), other.getGid())) {
            return false;
        }
        return true;
    }
}
