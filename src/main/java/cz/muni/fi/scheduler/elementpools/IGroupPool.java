package cz.muni.fi.scheduler.elementpools;

import cz.muni.fi.scheduler.elements.GroupElement;
import java.util.List;

/**
 * This is an interface for group pool.
 * @author Gabriela Podolnikova
 */
public interface IGroupPool {

    List<GroupElement> getGroups();
    
    GroupElement getGroup(int id);
}
