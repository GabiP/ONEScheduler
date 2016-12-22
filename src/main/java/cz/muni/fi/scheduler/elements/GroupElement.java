package cz.muni.fi.scheduler.elements;

import java.util.List;
import java.util.Objects;

/**
 * This class represents a group of users.
 * 
 * @author Gabriela Podolnikova
 */
public class GroupElement {

    private Integer id;
    
    private String name;
    
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
        return "GroupElement{" + "id=" + id + ", name=" + name + ", users=" + users + '}';
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 61 * hash + Objects.hashCode(this.id);
        hash = 61 * hash + Objects.hashCode(this.name);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final GroupElement other = (GroupElement) obj;
        return true;
    }
}
