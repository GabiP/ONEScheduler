package cz.muni.fi.xml.pools;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import cz.muni.fi.scheduler.elementpools.IGroupPool;
import cz.muni.fi.scheduler.elements.GroupElement;
import cz.muni.fi.xml.mappers.GroupXmlMapper;
import cz.muni.fi.xml.resources.lists.GroupXmlList;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import org.mapstruct.factory.Mappers;

/**
 *
 * @author Gabriela Podolnikova
 */
public class GroupXmlPool implements IGroupPool{

    private List<GroupElement> groups;

    GroupXmlMapper groupXmlMapper = Mappers.getMapper(GroupXmlMapper.class);
    
    public GroupXmlPool(String groupPoolPath) throws IOException {
        XmlMapper xmlMapper = new XmlMapper();
        String groupPoolMessage = new String(Files.readAllBytes(Paths.get(groupPoolPath)));
        GroupXmlList xmlList = xmlMapper.readValue(groupPoolMessage, GroupXmlList.class);
        groups = groupXmlMapper.map(xmlList.getGroups());
    }
    
    @Override
    public List<GroupElement> getGroups() {
        return Collections.unmodifiableList(groups);
    }

    @Override
    public GroupElement getGroup(int id) {
       for (GroupElement u : groups) {
            if (u.getId() == id) {
                return u;
            }
        }
        return null;
    }

}
