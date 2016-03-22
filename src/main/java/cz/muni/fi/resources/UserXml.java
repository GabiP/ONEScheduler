package cz.muni.fi.resources;

import org.opennebula.client.user.User;

/**
 *
 * @author Gabriela Podolnikova
 */
public class UserXml {
    
    private Integer id;
    
    private Integer gid;

    private User user;
    
    public UserXml (User user) {
        this.user = user;
        user.info();
        this.init();
    }
    
    public void init() {
        id = Integer.parseInt(user.xpath("/USER/ID"));
        gid = Integer.parseInt(user.xpath("/USER/ID"));
    }
    
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

}
