package cz.muni.fi.scheduler;

import cz.muni.fi.authorization.IAuthorizationManager;
import cz.muni.fi.scheduler.elementpools.IClusterPool;
import cz.muni.fi.scheduler.elementpools.IDatastorePool;
import cz.muni.fi.scheduler.elementpools.IHostPool;
import cz.muni.fi.scheduler.elementpools.IVmPool;
import cz.muni.fi.scheduler.fairshare.FairshareFactory;
import cz.muni.fi.scheduler.fairshare.AbstractPriorityCalculator;
import cz.muni.fi.scheduler.filters.FilterFactory;
import cz.muni.fi.scheduler.filters.datastores.IDatastoreFilter;
import cz.muni.fi.scheduler.resources.HostElement;
import cz.muni.fi.scheduler.resources.VmElement;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import cz.muni.fi.scheduler.filters.hosts.IHostFilter;
import cz.muni.fi.scheduler.policies.PolicyFactory;
import cz.muni.fi.scheduler.policies.datastores.IStoragePolicy;
import cz.muni.fi.scheduler.policies.datastores.StoragePacking;
import cz.muni.fi.scheduler.policies.hosts.IPlacementPolicy;
import cz.muni.fi.scheduler.policies.hosts.Packing;

/**
 * This class contains the main method.
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
    
    private static boolean preferHostFit;
    
    private static String secret;
    
    private static String endpoint;
    
    private static String hostPoolPath;
    
    private static String clusterPoolPath;
    
    private static String userPoolPath;
    
    private static String vmPoolPath;
    
    private static String datastorePoolPath;
    
    private static int cycleinterval;
    
    private static int numberofqueues;
    
    private static String[] hostFilters;
    
    private static String[] datastoreFilters;
    
    private static String[] hostPolicies;
    
    private static String[] datastorePolicies;
    
    private static String fairshare;
    
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
        preferHostFit = configuration.getBoolean("preferHostFit");
        endpoint = configuration.getString("endpoint");
        secret = configuration.getString("secret");
        hostPoolPath = configuration.getString("hostpoolpath");
        clusterPoolPath = configuration.getString("clusterpoolpath");
        userPoolPath = configuration.getString("userpoolpath");
        vmPoolPath = configuration.getString("vmpoolpath");
        datastorePoolPath = configuration.getString("datastorepoolpath");
        cycleinterval = configuration.getInt("cycleinterval");
        numberofqueues = configuration.getInt("numberofqueues");
        hostFilters = configuration.getStringArray("hostFilters");
        datastoreFilters = configuration.getStringArray("datastoreFilters");
        hostPolicies = configuration.getStringArray("hostPolicies");
        datastorePolicies = configuration.getStringArray("datastorePolicies");
        fairshare = configuration.getString("fairshare");
        
        List<IHostFilter> listHostFilters = FilterFactory.getHostFilters(hostFilters);
        List<IDatastoreFilter> listDatastoreFilters = FilterFactory.getDatastoreFilters(datastoreFilters);
        List<IPlacementPolicy> listPlacementPolicies = PolicyFactory.getPlacementPolicies(hostPolicies);
        List<IStoragePolicy> listStoragePolicy = PolicyFactory.getStoragePolicies(datastorePolicies);
        AbstractPriorityCalculator fairsharePolicy = FairshareFactory.getFairshare(fairshare);
        
        //check if policies are set, if not use default.
        if (listPlacementPolicies.isEmpty()) {
            IPlacementPolicy defaultPolicy = new Packing();
            listPlacementPolicies.add(defaultPolicy);
        }
        if (listStoragePolicy.isEmpty()) {
            IStoragePolicy defaultPolicy = new StoragePacking();
            listStoragePolicy.add(defaultPolicy);
        }
        
        if (useXml) {
            manager = new ManagerXML(hostPoolPath, clusterPoolPath, userPoolPath, vmPoolPath, datastorePoolPath);
            resultManager = new XmlResultManager(hostPoolPath, clusterPoolPath, userPoolPath, vmPoolPath, datastorePoolPath);
        } else {
            try {
                manager = new ManagerOne(secret, endpoint);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                System.out.println("Could not reach OpenNebula. Check if endpoint or secret has the right configuration.");
                return;
            }
        }
        
        while (true) {
            try {
                Scheduler scheduler = new Scheduler(manager, resultManager, listHostFilters, listDatastoreFilters, listPlacementPolicies, listStoragePolicy, fairsharePolicy, numberofqueues, preferHostFit);
                
                List<Match> plan = scheduler.schedule();
                
                /*boolean writeResultsSuccess = scheduler.storeResults();
                if (!writeResultsSuccess) {
                System.out.println("Could not store results into XML file.");
                }*/
                printPlan(plan);
                TimeUnit.SECONDS.sleep(cycleinterval);
            } catch (IOException | InterruptedException ex) {
                Logger.getLogger(SetUp.class.getName()).log(Level.SEVERE, null, ex);
            }
        }       
    }
    
    private static void printPlan(List<Match> plan ) {
        if (plan == null) {
            System.out.println("No schedule.");
            return;
        }
        for (Match match : plan) {
            System.out.println(match.getHost());
            System.out.println("Its vms: ");
            for (VmElement vm : match.getVms()) {
                System.out.println(vm);
            }
            System.out.println();
        }
    }

    
}
