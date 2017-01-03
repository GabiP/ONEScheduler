package cz.muni.fi.one.oned;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import cz.muni.fi.scheduler.setup.SetUp;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class loads the TMAD configuration given in the configFiles package.
 * 
 * @author Gabriela Podolnikova
 */
public class TmMadConfiguration {

    private static final List<TmMad> tmMadList;
    
    static {
        try {
            tmMadList = getTmMadConfig();
        } catch (Exception e) {
            throw new ExceptionInInitializerError(e);
        }
    }   
        
    private static final String CONFIG_DIRECTORY = "configFiles";
    private static final String TM_MAD_CONF_FILE_NAME = "tmMadConf.xml";

    protected static final Logger log = LoggerFactory.getLogger(SetUp.class);
    
    /**
     * Loads all the tmMad configuration.
     * @return the lists with tmMads.
     * @throws IOException 
     */
    private static List<TmMad> getTmMadConfig() throws IOException {
        XmlMapper xmlMapper = new XmlMapper();
        String tmMadMessage = new String(Files.readAllBytes(Paths.get(CONFIG_DIRECTORY + File.separator + TM_MAD_CONF_FILE_NAME)));
        TmMadList xmlList = xmlMapper.readValue(tmMadMessage, TmMadList.class);
        return xmlList.getTmMadList();
    }
    
    public static List<TmMad> getTmMadConfiguration() {
        return Collections.unmodifiableList(tmMadList);
    }
    
    /**
     * Returns whether the Datastore with the given tmMad name is shared or not.
     * @param tmMadName the name of the driver
     * @return the "YES" string for shared Datastore or "NO" if the given Datastore is not shared.
     */
    public static String getSharedInfo(String tmMadName) {
        for (TmMad tmMad: tmMadList) {
            String name = tmMad.getName();
            if (tmMadName.equals(name)) {
                return tmMad.getShared();
            }
        }
        log.info("No such Transfer Manager Driver found with name: " + tmMadName);
        return null;
    }
    
    /**
     * Returns the Clone target info.
     * @param tmMadName the name of the driver
     * @return the configuration for the given driver name.
     */
    public static String getCloneTargetInfo(String tmMadName) {
        for (TmMad tmMad: tmMadList) {
            String name = tmMad.getName();
            if (tmMadName.equals(name)) {
                return tmMad.getCloneTarget();
            }
        }
        log.info("No such Transfer Manager Driver found with name: " + tmMadName);
        return null;
    }
    
    /**
     * Returns the Ln target info.
     * @param tmMadName the name of the driver
     * @return the configuration for the given driver name.
     */
    public static String getLnTargetInfo(String tmMadName) {
        for (TmMad tmMad: tmMadList) {
            String name = tmMad.getName();
            if (tmMadName.equals(name)) {
                return tmMad.getLnTarget();
            }
        }
        log.info("No such Transfer Manager Driver found with name: " + tmMadName);
        return null;
    }
}
