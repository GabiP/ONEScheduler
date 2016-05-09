package cz.muni.fi.resources;

import java.util.ArrayList;
import org.opennebula.client.user.User;

/**
 *
 * @author Gabriela Podolnikova
 */
public class UserXml {
    
    private Integer id;
    
    private Integer gid;
    
    private ArrayList<Integer> groups;

    private User user;
    
    public UserXml (User user) {
        this.user = user;
        user.info();
        this.init();
    }
    
    public void init() {
        id = Integer.parseInt(user.xpath("/USER/ID"));
        gid = Integer.parseInt(user.xpath("/USER/ID"));
        getGroups("/USER/GROUPS");
    }
    
    public void getGroups(String xpathExpr) {
        groups = new ArrayList<>();
        int i = 1;
        String node = user.xpath(xpathExpr + "/ID["+i+"]");
        while (!node.equals("")) {
            Integer id= Integer.parseInt(user.xpath(xpathExpr + "/ID["+i+"]"));
            System.out.println("gorup id: " + id);
            i++;
            node = user.xpath(xpathExpr + "/ID["+i+"]");
            groups.add(id);
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
    public ArrayList<Integer> getGroups() {
        return groups;
    }

}
