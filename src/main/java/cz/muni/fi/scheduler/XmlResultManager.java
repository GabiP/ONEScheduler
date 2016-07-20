package cz.muni.fi.scheduler;

import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import cz.muni.fi.scheduler.resources.HostElement;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class stores the results from scheduling to the original xml files.
 * @author Gabriela Podolnikova
 */
public class XmlResultManager implements IResultManager {
    
    private final String hostPoolPath;
    private final String clusterPoolPath;
    private final String userPoolPath;
    private final String vmPoolPath;
    private final String datastorePoolPath;
    
    public XmlResultManager(String hostPoolPath, String clusterPoolPath, String userPoolPath, String vmPoolPath, String datastorePoolPath) {
         this.hostPoolPath = hostPoolPath;
         this.clusterPoolPath = clusterPoolPath;
         this.userPoolPath = userPoolPath;
         this.vmPoolPath = vmPoolPath;
         this.datastorePoolPath = datastorePoolPath;
    }
    
    @Override
    public boolean writeResults(List<HostElement> hostPool) {
        XmlMapper xmlMapper = new XmlMapper();
        try {
            xmlMapper.writer().withRootName(PropertyName.construct("HOSTPOOL")).writeValue(new File(hostPoolPath), hostPool);
        } catch (IOException ex) {
            Logger.getLogger(XmlResultManager.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }
}
