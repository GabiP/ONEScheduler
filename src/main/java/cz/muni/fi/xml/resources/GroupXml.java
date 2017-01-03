package cz.muni.fi.xml.resources;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.util.List;

/**
 * This class represents the User retrived from xml.
 * @author Gabriela Podolnikova
 */
public class GroupXml {

    @JacksonXmlProperty(localName = "ID")
    private Integer id;
    
    @JacksonXmlProperty(localName = "NAME")
    private String name;
    
    @JacksonXmlProperty(localName = "USERS")
    private List<Integer> users;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Integer> getUsers() {
        return users;
    }

    public void setUsers(List<Integer> users) {
        this.users = users;
    }

    @Override
    public String toString() {
        return "GroupXml{" + "id=" + id + ", name=" + name + ", users=" + users + '}';
    }  
}
