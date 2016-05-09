/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package authorization;

import cz.muni.fi.pools.AclXmlPool;
import cz.muni.fi.pools.ClusterXmlPool;
import cz.muni.fi.pools.DatastoreXmlPool;
import cz.muni.fi.pools.HostXmlPool;
import cz.muni.fi.pools.UserXmlPool;
import cz.muni.fi.resources.ClusterXml;
import cz.muni.fi.resources.DatastoreXml;
import cz.muni.fi.resources.HostXml;
import cz.muni.fi.resources.VmXml;
import java.util.ArrayList;
import java.util.List;
import org.opennebula.client.acl.Acl;

/**
 *
 * @author Gabriela Podolnikova
 */
public class AuthorizationManager {
    
    private final AclXmlPool aclPool;
    private final ClusterXmlPool clusterPool;
    private final HostXmlPool hostPool;
    private final DatastoreXmlPool datastorePool;
    private final UserXmlPool userPool;
    
    public AuthorizationManager(AclXmlPool acls, ClusterXmlPool clusters, HostXmlPool hosts, DatastoreXmlPool datastores, UserXmlPool userPool) {
        this.aclPool = acls;
        this.clusterPool = clusters;
        this.hostPool = hosts;
        this.datastorePool = datastores;
        this.userPool = userPool;
    }
    
    /**
     * Finds the subset of hosts where the owner of specified virtual machine can deploy.
     * 
     * @param vm the virtual machine's user's to be authorized
     * @return an array with ids of authorized hosts
     */
    public ArrayList<Integer> authorize(VmXml vm) {
        Integer uid = vm.getUid();
        List<Integer> userGroups = userPool.getById(uid).getGroups();
        //group id from virtual machine added to user's group ids - does that work?
        userGroups.add(vm.getGid());
        ArrayList<String> groups = new ArrayList<>();
        for (Integer gid: userGroups) {
            String gidstring = "@" + gid;
            groups.add(gidstring);
        }
        ArrayList<Acl> acls = aclPool.getAcls();
        ArrayList<Integer> authorizedHosts = new ArrayList<>();
        ArrayList<Integer> authorizedDatastores = new ArrayList<>();
        String uidstring = "#" + uid;
        for (Acl acl: acls) {
            String rule = acl.toString();
            String[] splittedRule = rule.split("\\s+");
            // check if the rule contains the user's id or one of the user's group id
            if ((splittedRule[0].trim().equals(uidstring) || groups.contains(splittedRule[0].trim()))) {
                // affected resources hosts with rights to manage
                if (splittedRule[2].contains("MANAGE") && splittedRule[1].contains("HOST")) {
                    if (splittedRule[1].contains("*")) {
                        authorizedHosts.addAll(hostPool.getHostsIds());
                    }
                    if (splittedRule[1].contains("#")) {
                        String s = splittedRule[1].substring(splittedRule[1].indexOf("#") + 1);
                        Integer hostId  = Integer.valueOf(s);
                        HostXml host = hostPool.getById(hostId);
                        authorizedHosts.add(host.getId());
                    }
                    if (splittedRule[1].contains("%")) {
                        String s = splittedRule[1].substring(splittedRule[1].indexOf("%") + 1);
                        Integer clusterId  = Integer.valueOf(s);
                        ClusterXml cl = clusterPool.getById(clusterId);
                        authorizedHosts.addAll(cl.getHosts());
                    }
                    
                }
                // Finding the datastores that the user is authorized to use
                if (splittedRule[2].contains("USE") && splittedRule[1].contains("DATASTORE")) {
                    if (splittedRule[1].contains("*")) {
                        authorizedDatastores.addAll(datastorePool.getDatastoresIds());
                    }
                    if (splittedRule[1].contains("#")) {
                        String s = splittedRule[1].substring(splittedRule[1].indexOf("#") + 1);
                        Integer dsId  = Integer.valueOf(s);
                        authorizedDatastores.add(dsId);
                    }
                    if (splittedRule[1].contains("%")) {
                        String s = splittedRule[1].substring(splittedRule[1].indexOf("%") + 1);
                        Integer clusterId  = Integer.valueOf(s);
                        ClusterXml cl = clusterPool.getById(clusterId);
                        authorizedDatastores.addAll(cl.getDatastores());
                    }
                }
            }
        }
        //match authorizedHosts and authorizedDatastores
        for (Integer hostId: authorizedHosts) {
            boolean hasSystemDs = false;
            HostXml host = hostPool.getById(hostId);
            Integer hostClusterId = host.getClusterId();
            ClusterXml cluster = clusterPool.getById(hostClusterId);
            List<Integer> clusterDatastores = cluster.getDatastores();
            for (Integer datastoreId: clusterDatastores) {
                DatastoreXml ds = datastorePool.findById(datastoreId);
                // the ds on cluster is system and the user is authorized to use that ds
                if (ds.getType() == 1 && authorizedDatastores.contains(ds.getId())) {
                    hasSystemDs = true;
                }               
            }
            if (hasSystemDs == false) {
                authorizedHosts.remove(hostId);
            }
        }
        return authorizedHosts;
    }
}
