package cz.muni.fi.one.mappers;

import cz.muni.fi.one.XpathLoader;
import cz.muni.fi.scheduler.elements.UserElement;
import cz.muni.fi.scheduler.elements.nodes.DatastoreQuota;
import cz.muni.fi.scheduler.elements.nodes.VmQuota;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.opennebula.client.user.User;

/**
 * This class maps OpenNebula's User class to UserElement class.
 * Retreives from OpenNebula's User instance its attributes by using OpenNebula's Java API.
 * For further information of Java API please refer to: http://docs.opennebula.org/doc/4.14/oca/java/
 * 
 * @author Andras Urge
 */
public class UserMapper {
    
    public static UserElement map(User user) {
        UserElement result = new UserElement();
        user.info();
        
        result.setId(XpathLoader.getInt(user, "/USER/ID"));
        result.setGid(XpathLoader.getInt(user, "/USER/GID"));
        result.setGroups(XpathLoader.getIntList(user, "/USER/GROUPS/ID"));
        try { 
            result.setDatastoreQuota(XpathLoader.getNodeList(user, DatastoreQuota.class, "/USER/DATASTORE_QUOTA/DATASTORE"));
            result.setVmQuota(XpathLoader.getNode(user, VmQuota.class, "/USER/VM_QUOTA/VM"));   
        } catch (InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(HostMapper.class.getName()).log(Level.SEVERE, null, ex);
        }   
        return result;
    }
}
