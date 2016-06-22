/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.xml.mappers;

import cz.muni.fi.scheduler.resources.UserElement;
import cz.muni.fi.xml.resources.UserXml;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Andras Urge
 */
public class UserXmlMapper {
    
    public static List<UserElement> map(UserXml[] users) {
        List<UserElement> result = new ArrayList<>();
        for(UserXml xml : users) {
            result.add(map(xml));
        }        
        return result;
    }
    
    public static UserElement map(UserXml user) {
        UserElement result = new UserElement();
        result.setId(user.getId());
        result.setGid(user.getGid());
        result.setGroups(user.getGroups());
        
        return result;
    }
}
