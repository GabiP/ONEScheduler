package cz.muni.fi.extensions;

import cz.muni.fi.scheduler.elements.UserElement;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * This class contains only static methods that are frequently used.
 * 
 * @author Andras Urge
 */
public class UserListExtension {
    
    /**
     * Gets only ids from given users.
     * @param users to get the id from.
     * @return set of users' ids.
     */
    public static Set<Integer> getUserIds(List<UserElement> users) {
        return users.stream().map(UserElement::getId).collect(Collectors.toSet());
    }
    
     public static List<Integer> getListUserIds(List<UserElement> users) {
        return users.stream().map(UserElement::getId).collect(Collectors.toList());
    }
}
