/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.one.pools;

import cz.muni.fi.one.mappers.UserMapper;
import cz.muni.fi.scheduler.elementpools.IUserPool;
import cz.muni.fi.scheduler.resources.UserElement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.opennebula.client.Client;
import org.opennebula.client.OneResponse;
import org.opennebula.client.user.User;
import org.opennebula.client.user.UserPool;

/**
 *
 * @author Gabriela Podolnikova
 */
public class UserElementPool implements IUserPool{
    
    private UserPool up;
    
    public UserElementPool(Client oneClient) {
        up = new UserPool(oneClient);
    }
    
    @Override
    public List<UserElement> getUsers() {
        List<UserElement> users = new ArrayList<>();
        OneResponse upr = up.info();
        if (upr.isError()) {
            //TODO: log it
            System.out.println(upr.getErrorMessage());
        }
        Iterator<User> itr = up.iterator();
        while (itr.hasNext()) {
            User element = itr.next();
            System.out.println("User: " + element);
            UserElement u = UserMapper.map(element);
            System.out.println("User: " + u);
            getUsers().add(u);
        }
        return users;
    }
    
    @Override
    public UserElement getById(int id) {
        return UserMapper.map(up.getById(id));
    }

}
