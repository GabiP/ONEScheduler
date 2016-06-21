package cz.muni.fi.scheduler;

import cz.muni.fi.authorization.AuthorizationManager;
import cz.muni.fi.one.pools.AclElementPool;
import cz.muni.fi.one.pools.ClusterElementPool;
import cz.muni.fi.one.pools.DatastoreElementPool;
import cz.muni.fi.one.pools.HostElementPool;
import cz.muni.fi.one.pools.UserElementPool;
import cz.muni.fi.one.pools.VmElementPool;
import cz.muni.fi.scheduler.elementpools.IAclPool;
import cz.muni.fi.scheduler.elementpools.IClusterPool;
import cz.muni.fi.scheduler.elementpools.IDatastorePool;
import cz.muni.fi.scheduler.elementpools.IHostPool;
import cz.muni.fi.scheduler.elementpools.IUserPool;
import cz.muni.fi.scheduler.elementpools.IVmPool;
import cz.muni.fi.scheduler.resources.HostElement;
import cz.muni.fi.scheduler.resources.VmElement;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.opennebula.client.Client;

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
            String SECRET = "oneadmin:opennebula";
            String ENDPOINT = "http://one-sendbox:2633/RPC2";

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
        List<VmElement> pendingVms = vmPool.getVmsByState(1);
        if (pendingVms.isEmpty()) {
            System.out.println("No pendings");
        }
        // VM queue construction
        queue.addAll(pendingVms);
        Map<HostElement, List<VmElement>> plan = processQueue(queue);
        for (HostElement host: plan.keySet()) {
            System.out.println(host);
            System.out.println("Its vms: ");
            for (VmElement vm: plan.get(host)) {
                System.out.println(vm);
            }
        }
    }

    public Map<HostElement, List<VmElement>> processQueue(LinkedList queue) {
        Map<HostElement, List<VmElement>> plan = new HashMap<>();
        while (!queue.isEmpty()) {
            VmElement vm = (VmElement) queue.peek();
            //check the authorization for this VM
            List<Integer> authorizedHosts = authorizationManager.authorize(vm);
            if (authorizedHosts.isEmpty()) {
                System.out.println("Empty authorized hosts.");
            }
            //filter authorized hosts for vm
            List<HostElement> filteredHosts = filterAuthorizedHosts(authorizedHosts, vm);
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
    
    public List<HostElement> filterAuthorizedHosts(List<Integer> authorizedHosts, VmElement vm) {
        List<HostElement> filteredHosts = new ArrayList<>();
        for (Integer hostId : authorizedHosts) {
            HostElement h = hostPool.getHost(hostId);
            if (match(h, vm)) {
                filteredHosts.add(h);
            }
        }
        return filteredHosts;
    }
    
    public void putValueToMap(Map<HostElement, List<VmElement>> plan, HostElement host, VmElement vm) {
        if (plan.containsKey(host)) {
            plan.get(host).add(vm);
        } else {
            List<VmElement> values = new ArrayList<>();
            values.add(vm);
            plan.put(host, values);
        }              
    }
    
    public boolean match(HostElement h, VmElement vm) {
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
    
    /*public List<VmElement> deployPlan(Map<HostElement, List<VmElement>> plan) {
        List<VmElement> failedVms = new ArrayList<>();
        Set<HostElement> hosts = plan.keySet();
        for (HostElement host: hosts) {
            List<VmElement> vms = plan.get(host);
            for (VmElement vm: vms) {
                OneResponse oneResp = .deploy(host.getId());
                if (oneResp.getMessage() == null) {
                    //Log error in deployment
                    System.out.println(oneResp.getErrorMessage());
                    failedVms.add(vm);
                }
            }
        }
        return failedVms;
    }*/
    
    public void loadOnePools() throws IOException {
        vmPool = new VmElementPool(oneClient);
        hostPool = new HostElementPool(oneClient);
        userPool = new UserElementPool(oneClient);
        aclPool = new AclElementPool(oneClient);
        clusterPool = new ClusterElementPool(oneClient);
        dsPool = new DatastoreElementPool(oneClient);
    }
}
