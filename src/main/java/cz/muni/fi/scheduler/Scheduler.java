package cz.muni.fi.scheduler;

import cz.muni.fi.authorization.AuthorizationManager;
import cz.muni.fi.one.pools.AclXmlPool;
import cz.muni.fi.one.pools.ClusterXmlPool;
import cz.muni.fi.one.pools.DatastoreXmlPool;
import cz.muni.fi.one.pools.HostXmlPool;
import cz.muni.fi.one.pools.UserXmlPool;
import cz.muni.fi.one.pools.VmXmlPool;
import cz.muni.fi.scheduler.elementpools.IAclPool;
import cz.muni.fi.scheduler.elementpools.IClusterPool;
import cz.muni.fi.scheduler.elementpools.IDatastorePool;
import cz.muni.fi.scheduler.elementpools.IHostPool;
import cz.muni.fi.scheduler.elementpools.IUserPool;
import cz.muni.fi.scheduler.elementpools.IVmPool;
import cz.muni.fi.scheduler.resources.HostXml;
import cz.muni.fi.scheduler.resources.VmXml;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.opennebula.client.Client;
import org.opennebula.client.OneResponse;

/**
 *
 * @author Gabriela Podolnikova
 */
public class Scheduler {
    
    Client oneClient;
    
    AuthorizationManager authorizationManager;
    
    IHostPool hostPool;
    
    IVmPool vmPool;
    
    IUserPool userPool;
    
    IAclPool aclPool;
    
    IClusterPool clusterPool;
    
    IDatastorePool dsPool;
    
    /**
     * Queues with waiting VMs.
     * Note: Load number of queues from configuration file.
     * We will use list of LinkedLists for multiple queues.
     * For concurrent queues we can use: ConcurrentLinkedQueue
     */
    private LinkedList queue = new LinkedList();
    
    public void init() {
        try {
            String SECRET = "";
            String ENDPOINT = "";

            oneClient = new Client(SECRET, ENDPOINT);
            
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    
    public void body() throws InterruptedException, IOException {
        //load all pools - must be the first call
        loadOnePools();
        //instantiate the Authorizationmanager
        authorizationManager = new AuthorizationManager(aclPool, clusterPool, hostPool, dsPool, userPool);
        //get pendings, state = 1 is pending
        List<VmXml> pendingVms = vmPool.getVmsByState(1);
        if (pendingVms.isEmpty()) {
            System.out.println("No pendings");
        }
        // VM queue construction
        queue.addAll(pendingVms);
        Map<HostXml, List<VmXml>> plan = processQueue(queue);
    }

    public Map<HostXml, List<VmXml>> processQueue(LinkedList queue) {
        Map<HostXml, List<VmXml>> plan = new HashMap<>();
        while (!queue.isEmpty()) {
            VmXml vm = (VmXml) queue.peek();
            //check the authorization for this VM
            List<Integer> authorizedHosts = authorizationManager.authorize(vm);
            if (authorizedHosts.isEmpty()) {
                System.out.println("Empty authorized hosts.");
            }
            //filter authorized hosts for vm
            List<HostXml> filteredHosts = filterAuthorizedHosts(authorizedHosts, vm);
            // deploy if filtered hosts is not empty
            if (!filteredHosts.isEmpty()) {
                putValueToMap(plan, filteredHosts.get(0), vm);
                System.out.println("Deploying vm: " + vm.getVmId() + " on host: " + filteredHosts.get(0).getId());
                // addCapacity - id I add capacity, won't there be a problem with the parameters when it is actually deployed?
                // Should I make a copy of this parameters and ask for that parameter. And When I update the pools I also update the copy.
            }
            queue.poll();
        }
        return plan;
    }
    
    public List<HostXml> filterAuthorizedHosts(List<Integer> authorizedHosts, VmXml vm) {
        List<HostXml> filteredHosts = new ArrayList<>();
        for (Integer hostId : authorizedHosts) {
            HostXml h = hostPool.getHost(hostId);
            if (match(h, vm)) {
                filteredHosts.add(h);
            }
        }
        return filteredHosts;
    }
    
    public void putValueToMap(Map<HostXml, List<VmXml>> plan, HostXml host, VmXml vm) {
        if (plan.containsKey(host)) {
            plan.get(host).add(vm);
        } else {
            List<VmXml> values = new ArrayList<>();
            values.add(vm);
            plan.put(host, values);
        }              
    }
    
    public boolean match(HostXml h, VmXml vm) {
        boolean enoughCapacity = h.testCapacity(vm);
        if (enoughCapacity == false) {
            System.out.println("Host does not have enough capacity - CPU, MEM to host the vm.");
        }
        boolean enoughCapacityDs = h.testDs(vm, clusterPool, dsPool);
        if (enoughCapacityDs == false) {
            System.out.println("Host does not have enough capacity in DATASTORES to host the vm.");
        }
        boolean reqs = vm.evaluateSchedReqs(h);
        if (reqs == false) {
            System.out.println("Host does not satisfy the scheduling requirements.");
        }
        boolean pciFits = h.checkPci(vm);
        if (reqs == false) {
            System.out.println("Host does not have the specied PCI.");
        }
        return enoughCapacity && reqs && enoughCapacityDs && pciFits;
    }
    
    public List<VmXml> deployPlan(Map<HostXml, List<VmXml>> plan) {
        List<VmXml> failedVms = new ArrayList<>();
        Set<HostXml> hosts = plan.keySet();
        for (HostXml host: hosts) {
            List<VmXml> vms = plan.get(host);
            for (VmXml vm: vms) {
                OneResponse oneResp = vm.getVm().deploy(host.getId());
                if (oneResp.getMessage() == null) {
                    //Log error in deployment
                    System.out.println(oneResp.getErrorMessage());
                    failedVms.add(vm);
                }
            }
        }
        return failedVms;
    }
    
    public void loadOnePools() throws IOException {
        vmPool = new VmXmlPool(oneClient);
        hostPool = new HostXmlPool(oneClient);
        userPool = new UserXmlPool(oneClient);
        aclPool = new AclXmlPool(oneClient);
        clusterPool = new ClusterXmlPool(oneClient);
        dsPool = new DatastoreXmlPool(oneClient);
    }
}
