package cz.muni.fi.pools;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import java.util.Arrays;

/**
 *
 * @author Gabriela Podolnikova
 */
@JacksonXmlRootElement(localName = "USERPOOL")
public class Userpool {
    
    @JacksonXmlProperty(localName = "USER")
    @JacksonXmlElementWrapper(useWrapping = false)
    private User[] users;
    
    @Override
    public String toString() {
        return "Userpool{" +
                "users=" + Arrays.toString(getUsers()) +
                '}';
    }

    /**
     * @return the users
     */
    public User[] getUsers() {
        return users;
    }

    /**
     * @param users the users to set
     */
    public void setUsers(User[] users) {
        this.users = users;
    }
}
