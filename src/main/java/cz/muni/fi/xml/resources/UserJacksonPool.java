/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.xml.resources;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import java.util.Arrays;

/**
 *
 * @author Andras Urge
 */
@JacksonXmlRootElement(localName = "USERPOOL")
public class UserJacksonPool {
    @JacksonXmlProperty(localName = "USER")
    @JacksonXmlElementWrapper(useWrapping = false)
    private UserXml[] users;

    public UserXml[] getUsers() {
        return users;
    }

    public void setUsers(UserXml[] users) {
        this.users = users;
    }    
    
    @Override
    public String toString() {
        return "Userpool{" +
                "users=" + Arrays.toString(users) +
                '}';
    }
}
