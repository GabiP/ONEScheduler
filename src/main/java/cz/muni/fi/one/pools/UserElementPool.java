package cz.muni.fi.one.pools;

import cz.muni.fi.one.mappers.UserMapper;
import cz.muni.fi.scheduler.elementpools.IUserPool;
import cz.muni.fi.scheduler.elements.UserElement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.opennebula.client.Client;
import org.opennebula.client.OneResponse;
import org.opennebula.client.user.User;
import org.opennebula.client.user.UserPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class represents OpenNebula's UserPool containing all instances of users in the system.
 * The pool is accessed through OpenNebula's Client. The Client represents the connection with the core of OpenNebula.
 * Each OpenNebula's instance of User is mapped to our UserElement.
 * 
 * @author Gabriela Podolnikova
 */
public class UserElementPool implements IUserPool{
    
    protected final Logger log = LoggerFactory.getLogger(getClass());
    
    private UserPool up;
    
    private List<UserElement> users;
    
    public UserElementPool(Client oneClient) {
        up = new UserPool(oneClient);
        users = new ArrayList<>();
        OneResponse upr = up.info();
        if (upr.isError()) {
            log.error(upr.getErrorMessage());
        }
        Iterator<User> itr = up.iterator();
        while (itr.hasNext()) {
            User element = itr.next();
            UserElement u = UserMapper.map(element);
            users.add(u);
        }
    }
    
    @Override
    public List<UserElement> getUsers() {
        return Collections.unmodifiableList(users);
    }
    
    /**
     * Returns the specified user.
     * @param id the id of the desired user.
     * @return the user
     */
    @Override
    public UserElement getUser(int id) {
        for (UserElement user: users) {
            if (user.getId().equals(id)){
                return user;
            }
        }
        return null;
    }

}
