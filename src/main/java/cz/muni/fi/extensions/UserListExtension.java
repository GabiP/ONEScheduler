/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.extensions;

import cz.muni.fi.scheduler.resources.UserElement;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Andras Urge
 */
public class UserListExtension {
    
    public static Set<Integer> getUserIds(List<UserElement> users) {
        Set<Integer> userIds = new HashSet<>();
        for (UserElement user : users) {
            userIds.add(user.getId());
        }
        return userIds;
    }
}
