/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.one.pools;

import cz.muni.fi.scheduler.resources.UserXml;
import java.util.ArrayList;
import java.util.Iterator;
import org.opennebula.client.Client;
import org.opennebula.client.OneResponse;
import org.opennebula.client.user.User;
import org.opennebula.client.user.UserPool;

/**
 *
 * @author Gabriela Podolnikova
 */
public class UserXmlPool {
    
    private UserPool up;
    
    private ArrayList<UserXml> users;
    
    public UserXmlPool(Client oneClient) {
        up = new UserPool(oneClient);
    }
    
    public void loadUsers() {
        users = new ArrayList<>();
        OneResponse upr = getUp().info();
        if (upr.isError()) {
            //TODO: log it
            System.out.println(upr.getErrorMessage());
        }
        Iterator<User> itr = getUp().iterator();
        while (itr.hasNext()) {
            User element = itr.next();
            System.out.println("User: " + element);
            UserXml u = new UserXml(element);
            System.out.println("User: " + u);
            getUsers().add(u);
        }
    }
    
    public UserXml getById(Integer id) {
        for (UserXml user: users) {
            if (user.getId() == id) {
                return user;
            }
        }
        return null;
    }

    /**
     * @return the up
     */
    public UserPool getUp() {
        return up;
    }

    /**
     * @param up the up to set
     */
    public void setUp(UserPool up) {
        this.up = up;
    }

    /**
     * @return the users
     */
    public ArrayList<UserXml> getUsers() {
        return users;
    }

    /**
     * @param users the users to set
     */
    public void setUsers(ArrayList<UserXml> users) {
        this.users = users;
    }
}
