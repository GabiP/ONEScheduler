package cz.muni.fi.resources;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.opennebula.client.user.User;

/**
 *
 * @author Gabriela Podolnikova
 */
public class UserXml {
    
    private Integer id;
    
    private Integer gid;
    
    private List<Integer> groups;

    private final User user;
    
    public UserXml (User user) {
        this.user = user;
        user.info();
        this.init();
    }
    
    public void init() {
        id = Integer.parseInt(user.xpath("/USER/ID"));
        gid = Integer.parseInt(user.xpath("/USER/ID"));
        try {
            groups = NodeElementLoader.getNodeId(user, "/USER/GROUPS/ID");
        } catch (InstantiationException | IllegalAccessException ex) {
            // TODO: react on failure if needed
            Logger.getLogger(VmXml.class.getName()).log(Level.SEVERE, null, ex);
        }
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

    /**
     * @return the groups
     */
    public List<Integer> getGroups() {
        return groups;
    }

}
