/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.xml.resources.lists;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import cz.muni.fi.xml.resources.UserXml;
import java.util.List;

/**
 *
 * @author Andras Urge
 */
@JacksonXmlRootElement(localName = "USERPOOL")
public class UserXmlList {
    
    @JacksonXmlProperty(localName = "USER")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<UserXml> users;

    public List<UserXml> getUsers() {
        return users;
    }

    public void setUsers(List<UserXml> users) {
        this.users = users;
    }
}
