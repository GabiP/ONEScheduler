package cz.muni.fi.result;

import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import cz.muni.fi.scheduler.core.Match;
import cz.muni.fi.scheduler.resources.ClusterElement;
import cz.muni.fi.scheduler.resources.HostElement;
import cz.muni.fi.scheduler.resources.VmElement;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class stores the results from scheduling to the original xml files.
 * @author Gabriela Podolnikova
 */
public class XmlResultManager implements IResultManager {
    
    protected final org.slf4j.Logger log = LoggerFactory.getLogger(getClass());
    
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
    
    public boolean writeHostPool(List<HostElement> hostPool) {
        XmlMapper xmlMapper = new XmlMapper();
        try {
            xmlMapper.writer().withRootName(PropertyName.construct("HOSTPOOL")).writeValue(new File(hostPoolPath), hostPool);
        } catch (IOException ex) {
            log.error("HostPool failed to write correctly: " + ex);
            return false;
        }
        return true;
    }
    
    public boolean writeClusterPool(List<ClusterElement> clusterPool) {
        XmlMapper xmlMapper = new XmlMapper();
        try {
            xmlMapper.writer().withRootName(PropertyName.construct("HOSTPOOL")).writeValue(new File(clusterPoolPath), clusterPool);
        } catch (IOException ex) {
            log.error("HostPool failed to write correctly: " + ex);
            return false;
        }
        return true;
    }
    
    public boolean writePools(List<ClusterElement> clusterPool) {
        XmlMapper xmlMapper = new XmlMapper();
        try {
            xmlMapper.writer().withRootName(PropertyName.construct("HOSTPOOL")).writeValue(new File(clusterPoolPath), clusterPool);
        } catch (IOException ex) {
            log.error("HostPool failed to write correctly: " + ex);
            return false;
        }
        return true;
    }

    @Override
    public List<VmElement> deployPlan(List<Match> plan) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<VmElement> migrate(List<Match> migrations) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
