/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.authorization;

import cz.muni.fi.scheduler.elementpools.IAclPool;
import cz.muni.fi.scheduler.elementpools.IClusterPool;
import cz.muni.fi.scheduler.elementpools.IDatastorePool;
import cz.muni.fi.scheduler.elementpools.IHostPool;
import cz.muni.fi.scheduler.elementpools.IUserPool;
import cz.muni.fi.scheduler.resources.ClusterXml;
import cz.muni.fi.scheduler.resources.DatastoreXml;
import cz.muni.fi.scheduler.resources.HostXml;
import cz.muni.fi.scheduler.resources.VmXml;
import java.util.ArrayList;
import java.util.List;
import org.opennebula.client.acl.Acl;

/**
 *
 * @author Gabriela Podolnikova
 */
public class AuthorizationManager {
    
    private final IAclPool aclPool;
    private final IClusterPool clusterPool;
    private final IHostPool hostPool;
    private final IDatastorePool datastorePool;
    private final IUserPool userPool;
    
    public AuthorizationManager(IAclPool acls, IClusterPool clusters, IHostPool hosts, IDatastorePool datastores, IUserPool userPool) {
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
    public List<Integer> authorize(VmXml vm) {
        Integer uid = vm.getUid();
        List<Integer> userGroups = userPool.getById(uid).getGroups();
        //group id from virtual machine added to user's group ids - does that work?
        userGroups.add(vm.getGid());
        ArrayList<String> groups = new ArrayList<>();
        for (Integer gid: userGroups) {
            String gidstring = "@" + gid;
            groups.add(gidstring);
        }
        List<Acl> acls = aclPool.getAcls();
        ArrayList<Integer> authorizedHosts = new ArrayList<>();
        ArrayList<Integer> authorizedDatastores = new ArrayList<>();
        String uidstring = "#" + uid;
        for (Acl acl: acls) {
            String rule = acl.toString();
            String[] splittedRule = rule.split("\\s+");
            // check if the rule contains the user's id or one of the user's group id
            if ((splittedRule[0].trim().equals(uidstring) || groups.contains(splittedRule[0].trim())) ||splittedRule[0].trim().equals("*")) {
                // affected resources hosts with rights to manage
                List<Integer> evaluatedHosts = evaluateHost(splittedRule[2], splittedRule[1]);
                authorizedHosts.addAll(evaluatedHosts);
                // Finding the datastores that the user is authorized to use
                List<Integer> evaluatedDatastores = evaluateDatastore(splittedRule[2], splittedRule[1]);
                authorizedDatastores.addAll(evaluatedDatastores);
            }
        }
        //match authorizedHosts and authorizedDatastores
        List<Integer> result = matchHostsAndDatastores(authorizedHosts, authorizedDatastores);
        return result;
    }
    
    public List<Integer> evaluateHost(String rights, String resources) {
        List<Integer> authorizedHosts = new ArrayList<>();
        if (rights.contains("MANAGE") && resources.contains("HOST")) {
            if (resources.contains("*")) {
                authorizedHosts.addAll(hostPool.getHostsIds());
            }
            if (resources.contains("#")) {
                Integer hostId = getIdFromResources(resources, "#");
                HostXml host = hostPool.getHost(hostId);
                authorizedHosts.add(host.getId());
            }
            if (resources.contains("%")) {
                Integer clusterId = getIdFromResources(resources, "%");
                ClusterXml cl = clusterPool.getCluster(clusterId);
                authorizedHosts.addAll(cl.getHosts());
            }
        }
        return authorizedHosts;
    }
    
    public List<Integer> evaluateDatastore(String rights, String resources) {
        List<Integer> authorizedDatastores = new ArrayList<>();
        if (rights.contains("USE") && resources.contains("DATASTORE")) {
            if (resources.contains("*")) {
                authorizedDatastores.addAll(datastorePool.getDatastoresIds());
            }
            if (resources.contains("#")) {
                Integer dsId = getIdFromResources(resources, "#");
                authorizedDatastores.add(dsId);
            }
            if (resources.contains("%")) {
                Integer clusterId = getIdFromResources(resources, "%");
                ClusterXml cl = clusterPool.getCluster(clusterId);
                authorizedDatastores.addAll(cl.getDatastores());
            }
        }
        return authorizedDatastores;
    }
    
    public Integer getIdFromResources(String resources, String mark) {
        String s = resources.substring(resources.indexOf(mark) + 1);
        Integer id = Integer.valueOf(s);
        return id;
    }
    
    public List<Integer> matchHostsAndDatastores(List<Integer> authorizedHosts, List<Integer> authorizedDatastores) {
        List<Integer> result = new ArrayList<>(authorizedHosts);
        for (Integer hostId: authorizedHosts) {
            boolean hasSystemDs = false;
            HostXml host = hostPool.getHost(hostId);
            Integer hostClusterId = host.getClusterId();
            ClusterXml cluster = clusterPool.getCluster(hostClusterId);
            List<Integer> clusterDatastores = cluster.getDatastores();
            for (Integer datastoreId: clusterDatastores) {
                DatastoreXml ds = datastorePool.getDatastore(datastoreId);
                // the ds on cluster is system and the user is authorized to use that ds
                if (ds.getType() == 1 && authorizedDatastores.contains(ds.getId())) {
                    hasSystemDs = true;
                }               
            }
            if (hasSystemDs == false) {
                result.remove(hostId);
            }
        }
        return result;
    }
}
