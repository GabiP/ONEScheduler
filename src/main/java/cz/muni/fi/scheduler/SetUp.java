package cz.muni.fi.scheduler;

import cz.muni.fi.scheduler.filters.FilterFactory;
import cz.muni.fi.scheduler.filters.IFilter;
import cz.muni.fi.scheduler.resources.HostElement;
import cz.muni.fi.scheduler.resources.VmElement;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class containd the main method.
 * Loads the configuration file and retreives its attributes.
 * Creates instance of a manager depending on whether we use OpenNebula or our own xml files.
 * Then creates an instance of a Scheduler and starts the scheduling.
 * TODO: Storing the results.
 * 
 * @author Gabriela Podolnikova
 */
public class SetUp {
    
    private static Configuration configuration;
    
    private static boolean useXml;
    
    private static String secret;
    
    private static String endpoint;
    
    private static String hostPoolPath;
    
    private static String clusterPoolPath;
    
    private static String userPoolPath;
    
    private static String vmPoolPath;
    
    private static String datastorePoolPath;
    
    private static String[] filters;
    
    private static IManager manager;
    
    private static IResultManager resultManager;
    
    public static void main(String[] args) {
        try {
            configuration = new Configuration("configuration.properties");
        } catch (IOException e) {
            System.err.println("Could not load configuration file!" + e);
            return;
        }
        
        useXml = configuration.getBoolean("useXml");
        endpoint = configuration.getString("endpoint");
        secret = configuration.getString("secret");
        hostPoolPath = configuration.getString("hostpoolpath");
        clusterPoolPath = configuration.getString("clusterpoolpath");
        userPoolPath = configuration.getString("userpoolpath");
        vmPoolPath = configuration.getString("vmpoolpath");
        datastorePoolPath = configuration.getString("datastorepoolpath");
        filters = configuration.getStringArray("filters");
        List<IFilter> listfilters = getFilters(filters);
        
        if (useXml) {
            try {
                manager = new ManagerXML(hostPoolPath, clusterPoolPath, userPoolPath, vmPoolPath, datastorePoolPath);
            } catch (IOException ex) {
                System.err.println("Could not load xml files!" + ex);
                return;
            }
            resultManager = new XmlResultManager(hostPoolPath, clusterPoolPath, userPoolPath, vmPoolPath, datastorePoolPath);
        } else {
            if (secret == null || endpoint == null) {
                System.out.println("Could not reach OpenNebula. Check if endpoint or secret has the right configuration.");
                return;
            } else {
                manager = new ManagerOne(secret, endpoint);
            }
        }
        
        Scheduler scheduler = new Scheduler(manager.getVmPool(), manager.getHostPool(), manager.getClusterPool(), manager.getDatastorePool(), manager.getAuthorizationManager(), resultManager, listfilters);
        
        Map<HostElement, List<VmElement>> plan = scheduler.schedule();
        /*boolean writeResultsSuccess = scheduler.storeResults();
        if (!writeResultsSuccess) {
            System.out.println("Could not store results into XML file.");
        }*/
        printPlan(plan);
    }
    
    private static void printPlan(Map<HostElement, List<VmElement>> plan ) {
        for (HostElement host : plan.keySet()) {
            System.out.println(host);
            System.out.println("Its vms: ");
            for (VmElement vm : plan.get(host)) {
                System.out.println(vm);
            }
            System.out.println();
        }
    }

    public static List<IFilter> getFilters(String[] stringfilters) {
        List<IFilter> filters = new ArrayList<>();
        for (int i = 0; i < stringfilters.length; i++) {
            String filterString = stringfilters[i];
            if (!filterString.contains(".")) {
                filterString = "cz.muni.fi.scheduler.filters." + filterString;
            }
            IFilter f = FilterFactory.createFilter(filterString);
            filters.add(f);
        }
        return filters;
    }
}
