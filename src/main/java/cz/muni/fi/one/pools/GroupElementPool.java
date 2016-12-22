package cz.muni.fi.one.pools;

import cz.muni.fi.one.mappers.GroupMapper;
import cz.muni.fi.scheduler.elementpools.IGroupPool;
import cz.muni.fi.scheduler.elements.GroupElement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.opennebula.client.Client;
import org.opennebula.client.OneResponse;
import org.opennebula.client.group.Group;
import org.opennebula.client.group.GroupPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class represents OpenNebula's GroupPool containing all instances of groups in the system.
 * The pool is accessed through OpenNebula's Client. The Client represents the connection with the core of OpenNebula.
 * Each OpenNebula's instance of Group is mapped to our GroupElement.
 * @author Gabriela Podolnikova
 */
public class GroupElementPool implements IGroupPool {

    protected final Logger log = LoggerFactory.getLogger(getClass());
    
    private GroupPool gp;
    
    List<GroupElement> groups;
    
    public GroupElementPool(Client oneClient) {
        gp = new GroupPool(oneClient);
        groups = new ArrayList<>();
        OneResponse gpr = gp.info();
        if (gpr.isError()) {
            log.error(gpr.getErrorMessage());
        }
        Iterator<Group> itr = gp.iterator();
        while (itr.hasNext()) {
            Group element = itr.next();
            GroupElement u = GroupMapper.map(element);
            groups.add(u);
        }
    }

    @Override
    public List<GroupElement> getGroups() {
        return Collections.unmodifiableList(groups);
    }

    @Override
    public GroupElement getGroup(int id) {
        for (GroupElement g : groups) {
            if (g.getId() == id) {
                return g;
            }
        }
        return null;
    }

}
