package cz.muni.fi.scheduler;

import cz.muni.fi.authorization.IAuthorizationManager;
import cz.muni.fi.scheduler.elementpools.IClusterPool;
import cz.muni.fi.scheduler.elementpools.IDatastorePool;
import cz.muni.fi.scheduler.elementpools.IHostPool;
import cz.muni.fi.scheduler.elementpools.IVmPool;
import cz.muni.fi.scheduler.resources.HostElement;
import cz.muni.fi.scheduler.resources.VmElement;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Gabriela Podolnikova
 */
public class Scheduler {
    
    IAuthorizationManager authorizationManager;
    
    IHostPool hostPool;
    
    IVmPool vmPool;    
    
    IClusterPool clusterPool;
    
    IDatastorePool dsPool;
    
    List<VmElement> pendingVms; 
    
    /**
     * Queues with waiting VMs.
     * Note: Load number of queues from configuration file.
     * We will use list of LinkedLists for multiple queues.
     * For concurrent queues we can use: ConcurrentLinkedQueue
     */
    private LinkedList queue = new LinkedList();

    Scheduler(IVmPool vmPool, IHostPool hostPool, IClusterPool clusterPool, IDatastorePool dsPool, IAuthorizationManager authorizationManager) {
        this.vmPool = vmPool;
        this.hostPool = hostPool;
        this.clusterPool = clusterPool;
        this.dsPool = dsPool;
        this.authorizationManager = authorizationManager;
    }
    

    public Map<HostElement, List<VmElement>> getPlan() {
        //get pendings, state = 1 is pending
        pendingVms = vmPool.getVmsByState(1);
        if (pendingVms.isEmpty()) {
            System.out.println("No pendings");
        }
        // VM queue construction
        queue.addAll(pendingVms);
        Map<HostElement, List<VmElement>> plan = processQueue(queue);
        return plan;
    }

    public Map<HostElement, List<VmElement>> processQueue(LinkedList queue) {
        Map<HostElement, List<VmElement>> plan = new HashMap<>();
        List<Integer> authorizedHosts;
        while (!queue.isEmpty()) {
            VmElement vm = (VmElement) queue.peek();
            //check the authorization for this VM
            authorizedHosts = authorizationManager.authorize(vm);
            if (authorizedHosts.isEmpty()) {
                System.out.println("Empty authorized hosts.");
            }
            //filter authorized hosts for vm
            List<HostElement> filteredHosts = filterAuthorizedHosts(authorizedHosts, vm);
            // deploy if filtered hosts is not empty
            if (!filteredHosts.isEmpty()) {
                plan = putValueToMap(plan, filteredHosts.get(0), vm);
                System.out.println("Deploying vm: " + vm.getVmId() + " on host: " + filteredHosts.get(0).getId());
                // addCapacity
                filteredHosts.get(0).addCapacity(vm);
            }
            queue.poll();
        }
        return plan;
    }
    
    public List<HostElement> filterAuthorizedHosts(List<Integer> authorizedHosts, VmElement vm) {
        List<HostElement> filteredHosts = new ArrayList<>();
        for (Integer hostId : authorizedHosts) {
            HostElement h = hostPool.getCachedHosts(hostId);
            if (match(h, vm)) {
                filteredHosts.add(h);
            }
        }
        return filteredHosts;
    }
    
    public Map<HostElement, List<VmElement>> putValueToMap(Map<HostElement, List<VmElement>> plan, HostElement host, VmElement vm) {
        if (plan.containsKey(host)) {
            plan.get(host).add(vm);
        } else {
            List<VmElement> values = new ArrayList<>();
            values.add(vm);
            plan.put(host, values);
        }
        return plan;
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
    

}