package cz.muni.fi.scheduler.resources;

import java.util.List;

/**
 *
 * @author Gabriela Podolnikova
 */
public class UserElement {
    
    private Integer id;
    
    private Integer gid;
    
    private List<Integer> groups;
        
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
}
