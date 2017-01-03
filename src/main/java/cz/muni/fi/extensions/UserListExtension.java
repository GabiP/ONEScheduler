package cz.muni.fi.extensions;

import cz.muni.fi.scheduler.elements.UserElement;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Class containing helper methods for a List of UserElements
 * 
 * @author Andras Urge
 */
public class UserListExtension {
    
    /**
     * Returns the IDs of the users.
     * 
     * @param users list of users
     * @return set of IDs for the users
     */
    public static Set<Integer> getUserIds(List<UserElement> users) {
        return users.stream().map(UserElement::getId).collect(Collectors.toSet());
    }
    
     public static List<Integer> getListUserIds(List<UserElement> users) {
        return users.stream().map(UserElement::getId).collect(Collectors.toList());
    }
}
