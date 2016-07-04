/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.xml.pools;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import cz.muni.fi.scheduler.elementpools.IUserPool;
import cz.muni.fi.scheduler.resources.UserElement;
import cz.muni.fi.xml.mappers.UserXmlMapper;
import cz.muni.fi.xml.resources.UserXml;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Andras Urge
 */
@JacksonXmlRootElement(localName = "USERPOOL")
public class UserXmlPool implements IUserPool {

    @JacksonXmlProperty(localName = "USER")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<UserXml> users;
        
    @Override
    public List<UserElement> getUsers() {
        return Collections.unmodifiableList(UserXmlMapper.map(users));
    }

    @Override
    public UserElement getById(int id) {
        for (UserElement u : UserXmlMapper.map(users)) {
            if (u.getId() == id) {
                return u;
            }
        }
        return null;
    }    
}
