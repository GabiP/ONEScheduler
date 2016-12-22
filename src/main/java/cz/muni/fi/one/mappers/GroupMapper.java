package cz.muni.fi.one.mappers;

import cz.muni.fi.one.XpathLoader;
import cz.muni.fi.scheduler.elements.GroupElement;
import org.opennebula.client.group.Group;

/**
 * This class maps OpenNebula's Group class to GroupElement class.
 * Retreives from OpenNebula's Group instance its attributes by using OpenNebula's Java API.
 * For further information of Java API please refer to: https://docs.opennebula.org/5.2/integration/system_interfaces/java.html
 * @author Gabriela Podolnikova
 */
public class GroupMapper {
    
    public static GroupElement map(Group group) {
        GroupElement result = new GroupElement();
        group.info();
        
        result.setId(XpathLoader.getInt(group, "/GROUP/ID"));
        result.setName(group.xpath("/GROUP/NAME"));
        result.setUsers(XpathLoader.getIntList(group, "/GROUP/USERS/ID"));
        return result;
    }
}
