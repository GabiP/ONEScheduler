package cz.muni.fi.scheduler.elementpools;

import cz.muni.fi.scheduler.elements.UserElement;
import java.util.List;

/**
 * This is an inteface for user pool.
 * @author Gabriela Podolnikova
 */
public interface IUserPool {
    
    List<UserElement> getUsers();
    
    UserElement getUser(int id);
}
