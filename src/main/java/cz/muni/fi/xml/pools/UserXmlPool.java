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
import org.mapstruct.factory.Mappers;

/**
 *
 * @author Andras Urge
 */
public class UserXmlPool implements IUserPool {

    private List<UserElement> users;

    UserXmlMapper userXmlMapper = Mappers.getMapper(UserXmlMapper.class);
    
    public UserXmlPool(String userPoolPath) throws IOException {
        XmlMapper xmlMapper = new XmlMapper();
        String userPoolMessage = new String(Files.readAllBytes(Paths.get(userPoolPath)));
        UserXmlList xmlList = xmlMapper.readValue(userPoolMessage, UserXmlList.class);
        users = userXmlMapper.map(xmlList.getUsers());
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
