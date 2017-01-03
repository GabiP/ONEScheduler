package cz.muni.fi.scheduler.elementpools;

import java.util.List;
import org.opennebula.client.acl.Acl;

/**
 * A pool interface.
 * @author Gabriela Podolnikova
 */
public interface IAclPool {
    
    List<Acl> getAcls();
}
