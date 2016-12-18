/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.extensions;

import cz.muni.fi.scheduler.elements.UserElement;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 *
 * @author Andras Urge
 */
public class UserListExtension {
    
    public static Set<Integer> getUserIds(List<UserElement> users) {
        return users.stream().map(UserElement::getId).collect(Collectors.toSet());
    }
    
    public static Set<Integer> getUsersOfGroup(List<UserElement> users, Integer groupId) {
        Set<Integer> usersOfGroup = new HashSet<>();
        for (UserElement user: users) {
            List<Integer> userGroups = user.getGroups();
            if (userGroups.contains(groupId)) {
                usersOfGroup.add(user.getId());
            }
        }
        return usersOfGroup;
    }
}
