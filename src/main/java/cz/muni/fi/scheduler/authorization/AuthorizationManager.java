package cz.muni.fi.scheduler.authorization;

import cz.muni.fi.extensions.UserListExtension;
import cz.muni.fi.extensions.VmListExtension;
import cz.muni.fi.scheduler.elementpools.IAclPool;
import cz.muni.fi.scheduler.elementpools.IClusterPool;
import cz.muni.fi.scheduler.elementpools.IDatastorePool;
import cz.muni.fi.scheduler.elementpools.IHostPool;
import cz.muni.fi.scheduler.elementpools.IUserPool;
import cz.muni.fi.scheduler.elements.ClusterElement;
import cz.muni.fi.scheduler.elements.DatastoreElement;
import cz.muni.fi.scheduler.elements.HostElement;
import cz.muni.fi.scheduler.elements.VmElement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.opennebula.client.acl.Acl;

/**
 * Authorization manager for managing ACL obtained from OpenNebula.
 * For understanding OpenNebula ACLs please refer to http://docs.opennebula.org/4.14/administration/users_and_groups/manage_acl.html
 * 
 * @author Gabriela Podolnikova
 */
public class AuthorizationManager implements IAuthorizationManager {
    
    private final IAclPool aclPool;
    private final IClusterPool clusterPool;
    private final IHostPool hostPool;
    private final IDatastorePool datastorePool;
    private final IUserPool userPool;
    
    private Map<Integer, List<HostElement>> authorizedHostsByUser;
    private Map<Integer, List<DatastoreElement>> authorizedDatastoresByUser;
    
    public AuthorizationManager(IAclPool acls, IClusterPool clusters, IHostPool hosts, IDatastorePool datastores, IUserPool userPool) {
        this.aclPool = acls;
        this.clusterPool = clusters;
        this.hostPool = hosts;
        this.datastorePool = datastores;
        this.userPool = userPool;
        authorizedHostsByUser = new HashMap<>();
        authorizedDatastoresByUser = new HashMap<>();
    }
    
    /**
     * Finds the subset of hosts where the owner of specified virtual machine can deploy.
     * 
     * @param vm the virtual machine's user to be authorized
     * Sets authorized hosts and datastores.
     */
    @Override
    public void authorize(List<VmElement> vms) {
        List<Acl> acls = aclPool.getAcls();
        for (Acl acl: acls) {
            String rule = acl.toString();
            String[] splittedRule = rule.split("\\s+");
            //rule for user
            if (splittedRule[0].contains("#")) {
               Integer aclUid = Integer.valueOf(splittedRule[0].substring(1).trim());
               setAuthorizedHost(aclUid, evaluateHost(splittedRule[2], splittedRule[1]));
               setAuthorizedDatastores(aclUid, evaluateDatastore(splittedRule[2], splittedRule[1]));
            }
            //rule for group - get users of that group
            if (splittedRule[0].contains("@")) {
                Integer aclGid = Integer.valueOf(splittedRule[0].substring(1).trim());
                Set<Integer> usersOfGroup = UserListExtension.getUsersOfGroup(userPool.getUsers(), aclGid);
                setAuthorizedHostForMoreUsers(usersOfGroup, evaluateHost(splittedRule[2], splittedRule[1]));
                setAuthorizedDatastoreForMoreUsers(usersOfGroup, evaluateDatastore(splittedRule[2], splittedRule[1]));
            }
            //rule for all
            if (splittedRule[0].contains("*")) {
                Set<Integer> userIds = UserListExtension.getUserIds(userPool.getUsers());
                setAuthorizedHostForMoreUsers(userIds, evaluateHost(splittedRule[2], splittedRule[1]));
                setAuthorizedDatastoreForMoreUsers(userIds, evaluateDatastore(splittedRule[2], splittedRule[1]));
            }
        }
    }
    
    @Override
    public List<HostElement> getAuthorizedHosts(Integer id) {
        return authorizedHostsByUser.get(id);
    }
    
    @Override
    public List<DatastoreElement> getAuthorizedDs(Integer id) {
        return authorizedDatastoresByUser.get(id);
    }
    
    
    /**
     * Evaluates given acl rule - its rights and resources.
     * Use when evaluating user's rights to manage hosts.
     * 
     * @param rights the string representing the rights to be checked
     * @param resources the string representing the resources to be checked
     * @return a list of authorized hosts
     */
    private List<HostElement> evaluateHost(String rights, String resources) {
        List<Integer> authorizedHosts = new ArrayList<>();
        if (rights.contains("MANAGE") && resources.contains("HOST")) {
            if (resources.contains("*")) {
                authorizedHosts.addAll(hostPool.getHostsIds());
            }
            if (resources.contains("#")) {
                Integer hostId = getIdFromResources(resources, "#");
                HostElement host = hostPool.getHost(hostId);
                authorizedHosts.add(host.getId());
            }
            if (resources.contains("%")) {
                Integer clusterId = getIdFromResources(resources, "%");
                ClusterElement cl = clusterPool.getCluster(clusterId);
                authorizedHosts.addAll(cl.getHosts());
            }
        }
        return getHosts(authorizedHosts);
    }
    
     /**
     * Evaluates given acl rule - its rights and resources.
     * Use when evaluating user's rights to use datastore.
     * 
     * @param rights the string representing the rights to be checked
     * @param resources the string representing the resources to be checked
     * @return a list of authorized datastores
     */
    private List<DatastoreElement> evaluateDatastore(String rights, String resources) {
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
                ClusterElement cl = clusterPool.getCluster(clusterId);
                authorizedDatastores.addAll(cl.getDatastores());
            }
        }
        return getDatastores(authorizedDatastores);
    }
    
    private Integer getIdFromResources(String resources, String mark) {
        String s = resources.substring(resources.indexOf(mark) + 1);
        return Integer.valueOf(s);
    }
    
    private void setAuthorizedHostForMoreUsers(Set<Integer> userIds, List<HostElement> hosts) {
        for (Integer userId: userIds) {
            setAuthorizedHost(userId, hosts);
        }
    }
    
    private void setAuthorizedHost(Integer userId, List<HostElement> hosts) {
        if (!hosts.isEmpty()) {
            if (authorizedHostsByUser.containsKey(userId)) {
                authorizedHostsByUser.get(userId).addAll(hosts);
            } else {
                authorizedHostsByUser.put(userId, hosts);
            }
        }
    }
    private void setAuthorizedDatastoreForMoreUsers(Set<Integer> userIds, List<DatastoreElement> dss) {
        for (Integer userId: userIds) {
            setAuthorizedDatastores(userId, dss);
        }
    }
    
    private void setAuthorizedDatastores(Integer userId, List<DatastoreElement> dss) {
        if (!dss.isEmpty()) {
            if (authorizedDatastoresByUser.containsKey(userId)) {
                authorizedDatastoresByUser.get(userId).addAll(dss);
            } else {
                authorizedDatastoresByUser.put(userId, dss);
            }
        }
    }
    
    private List<HostElement> getHosts(List<Integer> hostsIds) {
        List<HostElement> hosts = new ArrayList<>();
        for (Integer id: hostsIds) {
            HostElement host = hostPool.getHost(id);
            if (host.isActive()) {
                hosts.add(host);
            }
        }
        return hosts;
    }
    
    private List<DatastoreElement> getDatastores(List<Integer> dssIds) {
        List<DatastoreElement> dss = new ArrayList<>();
        for (Integer id: dssIds) {
            DatastoreElement ds = datastorePool.getDatastore(id);
            if (ds.isSystem()) {
                dss.add(ds);
            }
        }
        return dss;
    }
}
