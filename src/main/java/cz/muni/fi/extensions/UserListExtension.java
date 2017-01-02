/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
}
