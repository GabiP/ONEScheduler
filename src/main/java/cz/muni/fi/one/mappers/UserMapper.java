/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.one.mappers;

import cz.muni.fi.one.XpathLoader;
import cz.muni.fi.scheduler.resources.UserElement;
import org.opennebula.client.user.User;

/**
 * This class maps OpenNebula's User class to UserElement class.
 * Retreives from OpenNebula's User instance its attributes by using OpenNebula's Java API.
 * For further information of Java API please refer to: http://docs.opennebula.org/doc/4.14/oca/java/
 * 
 * @author Andras Urge
 */
public class UserMapper {
    
    public static UserElement map(User user) {
        UserElement result = new UserElement();
        user.info();
        
        result.setId(XpathLoader.getInt(user, "/USER/ID"));
        result.setGid(XpathLoader.getInt(user, "/USER/GID"));
        result.setGroups(XpathLoader.getIntList(user, "/USER/GROUPS/ID"));
        
        return result;
    }
}
