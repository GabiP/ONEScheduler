/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.xml.pools;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import cz.muni.fi.scheduler.elementpools.IUserPool;
import cz.muni.fi.scheduler.elements.UserElement;
import cz.muni.fi.xml.mappers.UserXmlMapper;
import cz.muni.fi.xml.resources.lists.UserXmlList;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

/**
 * Class responsible for reading users from an XML file.
 *
 * @author Andras Urge
 */
public class UserXmlPool implements IUserPool {

    private List<UserElement> users;

    /**
     * Loads the users from a file.
     * 
     * @param userPoolPath path to the file
     * @throws IOException 
     */
    public UserXmlPool(String userPoolPath) throws IOException {
        XmlMapper xmlMapper = new XmlMapper();
        String userPoolMessage = new String(Files.readAllBytes(Paths.get(userPoolPath)));
        UserXmlList xmlList = xmlMapper.readValue(userPoolMessage, UserXmlList.class);
        users = UserXmlMapper.map(xmlList.getUsers());
    }
    
    /**
     * Gets all the users.
     * 
     * @return all the users
     */
    @Override
    public List<UserElement> getUsers() {
        return Collections.unmodifiableList(users);
    }

    /**
     * Gets a user by the provided ID.
     * 
     * @param id the user ID
     * @return user
     */
    @Override
    public UserElement getUser(int id) {
        for (UserElement u : users) {
            if (u.getId() == id) {
                return u;
            }
        }
        return null;
    }    
}
