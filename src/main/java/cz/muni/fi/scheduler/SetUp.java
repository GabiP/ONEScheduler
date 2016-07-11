/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.scheduler;

import cz.muni.fi.scheduler.resources.HostElement;
import cz.muni.fi.scheduler.resources.VmElement;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
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
    
    private static IManager manager;
    
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
        
        if (useXml) {
            try {
                manager = new ManagerXML(hostPoolPath, clusterPoolPath, userPoolPath, vmPoolPath, datastorePoolPath);
            } catch (IOException ex) {
                System.err.println("Could not load xml files!" + ex);
                return;
            }
        } else {
            if (secret == null || endpoint == null) {
                System.out.println("Could not reach OpenNebula. Check if endpoint or secret has the right configuration.");
                return;
            } else {
                manager = new ManagerOne(secret, endpoint);
            }
        }
        
        Scheduler scheduler = new Scheduler(manager.getVmPool(), manager.getHostPool(), manager.getClusterPool(), manager.getDatastorePool(), manager.getAuthorizationManager());
        
        Map<HostElement, List<VmElement>> plan = scheduler.getPlan();
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
}
