/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.xml.pools;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import cz.muni.fi.scheduler.elementpools.IUserPool;
import cz.muni.fi.scheduler.resources.UserElement;
import cz.muni.fi.xml.mappers.UserXmlMapper;
import cz.muni.fi.xml.resources.UserXmlList;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Andras Urge
 */
public class UserXmlPool implements IUserPool {

    private List<UserElement> users;

    public UserXmlPool(String userPoolPath) throws IOException {
        XmlMapper xmlMapper = new XmlMapper();
        String userPoolMessage = new String(Files.readAllBytes(Paths.get(userPoolPath)));
        UserXmlList xmlList = xmlMapper.readValue(userPoolMessage, UserXmlList.class);
        users = UserXmlMapper.map(xmlList.getUsers());
    }
    
    @Override
    public List<UserElement> getUsers() {
        return Collections.unmodifiableList(users);
    }

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
