package cz.muni.fi.scheduler;

import cz.muni.fi.authorization.IAuthorizationManager;
import cz.muni.fi.scheduler.elementpools.IClusterPool;
import cz.muni.fi.scheduler.elementpools.IDatastorePool;
import cz.muni.fi.scheduler.elementpools.IHostPool;
import cz.muni.fi.scheduler.elementpools.IUserPool;
import cz.muni.fi.scheduler.elementpools.IVmPool;
import cz.muni.fi.scheduler.filters.FilterFactory;
import cz.muni.fi.scheduler.filters.IFilter;
import cz.muni.fi.scheduler.resources.DatastoreElement;
import cz.muni.fi.scheduler.resources.HostElement;
import cz.muni.fi.scheduler.resources.VmElement;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * The class Scheduler is the core class responsible for all events during the scheduling.
 * Takes all pools in the system, the instance of AuthorizationManager and
 * infromation from configuration - Filters that will be used to filter hosts.
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
    
    List<HostElement> activeHosts;
    
    IResultManager resultManager;
    
    /**
     * This map is used for computing the cpu usages.
     * Every time we match a host with a virtual machine the cpu usage needs to be increased.
     * We are storing the used space.
     */
    Map<HostElement, Float> cpuUsages;
    
    /**
     * This map is used for computing the memory usages.
     * Every time we match a host with a virtual machine the memory usage needs to be increased.
     * We are storing the used space.
     */
    Map<HostElement, Integer> memoryUsages;
    
    /**
     * This map is used for computing the disk usages.
     * Every time we match a host with a virtual machine the disk usage needs to be decreased.
     * We are storing the free space.
     */
    Map<HostElement, Integer> diskUsages;
    
    /**
     * The list of filters to be used for matching the host for a virtual machine.
     */
    List<IFilter> filters;
    
    /**
     * Queues with waiting VMs.
     * Note: Load number of queues from configuration file.
     * We will use list of LinkedLists for multiple queues.
     * For concurrent queues we can use: ConcurrentLinkedQueue
     */
    private LinkedList queue = new LinkedList();

    Scheduler(IVmPool vmPool, IHostPool hostPool, IClusterPool clusterPool, IDatastorePool dsPool, IAuthorizationManager authorizationManager, IResultManager resultManager, List<IFilter> filters) {
        this.vmPool = vmPool;
        this.hostPool = hostPool;
        this.clusterPool = clusterPool;
        this.dsPool = dsPool;
        this.authorizationManager = authorizationManager;
        this.resultManager = resultManager;
        this.filters = filters;
    }
    
    /**
     * This method starts the scheduling.
     * Gets active hosts, initialize capacity maps, gets pending virtual machines and puts in the queues.
     * Calls fairshare and calls the method to process queues.
     * @return the map with the plan
     */
    public Map<HostElement, List<VmElement>> schedule() {
        //get active hosts
        activeHosts = hostPool.getActiveHosts();
        //initialize capacity maps
        cpuUsages = initializeCpuCapacity(activeHosts);
        memoryUsages = initializeMemoryCapacity(activeHosts);
        diskUsages = initializeDiskCapacity(activeHosts);
        //get pendings, state = 1 is pending
        pendingVms = vmPool.getVmsByState(1);
        if (pendingVms.isEmpty()) {
            System.out.println("No pendings");
            return null;
        }
        // VM queue construction
        queue.addAll(pendingVms);
        //TODO sort VM in queues by fairshare
        //process queue by queue or in parallel
        Map<HostElement, List<VmElement>> plan = processQueue(queue);
        return plan;
    }

    /**
     * This method takes the queue with pending virtual machines and process it.
     * For each vm:
     * - retrieves the authorized hosts
     * - filter hosts by specified filters in the configuration file
     * --> it creates a subset of hosts that are suitable for the virtual machine.
     * Calls scheduling policy to select the host.
     * If there is match, puts the values to a plan anf changes the capacities.
     * @param queue the queue to be processed
     * @return the map with the plan for one queue
     */
    public Map<HostElement, List<VmElement>> processQueue(LinkedList queue) {
        Map<HostElement, List<VmElement>> plan = new HashMap<>();
        List<Integer> authorizedHosts;
        while (!queue.isEmpty()) {
            VmElement vm = (VmElement) queue.peek();
            //check the authorization for this VM
            authorizedHosts = authorizationManager.authorize(vm);
            if (authorizedHosts.isEmpty()) {
                System.out.println("Empty authorized hosts.");
                queue.poll();
                continue;
            }
            //filter authorized hosts for vm
            List<HostElement> filteredHosts = filterAuthorizedHosts(authorizedHosts, vm);
            // deploy if filtered hosts is not empty
            if (!filteredHosts.isEmpty()) {
                HostElement chosenHost = filteredHosts.get(0);
                plan = putValueToMap(plan, chosenHost, vm);
                System.out.println("Deploying vm: " + vm.getVmId() + " on host: " + filteredHosts.get(0).getId());
                // change capacities
                cpuUsages = addCpuCapacity(chosenHost, vm, cpuUsages);
                memoryUsages = addMemoryCapacity(chosenHost, vm, memoryUsages);
                diskUsages = addDiskCapacity(chosenHost, vm, diskUsages);
            }
            queue.poll();
        }
        return plan;
    }
    
    /**
     * Filters hosts that are authorized for the specified vm.
     * Calls the filters for each host.
     * @param authorizedHosts the hosts to be tested.
     * @param vm the virtual machine to be tested
     * @return the list of filter hosts
     */
    public List<HostElement> filterAuthorizedHosts(List<Integer> authorizedHosts, VmElement vm) {
        List<HostElement> filteredHosts = new ArrayList<>();
        for (Integer hostId : authorizedHosts) {
            HostElement h = hostPool.getHost(hostId);
            /*if (match(h, vm)) {
                filteredHosts.add(h);
            }*/
            boolean matched = getResultsFromFilters(filters, h, vm);
            if (matched) {
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
    
    /**
     * Goes through all filters and calls the test if the vm and host matches by the specified criteria in the filter.
     * @param filters filters to be used
     * @param h the host to be tested
     * @param vm the virtual machine to be tested
     * @return true if the host and vm match, false othewise
     */
    public boolean getResultsFromFilters(List<IFilter> filters, HostElement h, VmElement vm) {
         boolean result = true;
         for (IFilter filter: filters) {
             result = result && filter.test(vm, h, clusterPool, dsPool, this);
         }
         return result;
     }
    
    public Map<HostElement, Float> initializeCpuCapacity(List<HostElement> hosts) {
        Map<HostElement, Float> usages = new HashMap<>();
        for (HostElement host: hosts) {
            usages.put(host, host.getCpu_usage());
        }
        return usages;
    }
    
    public Map<HostElement, Integer> initializeMemoryCapacity(List<HostElement> hosts) {
        Map<HostElement, Integer> usages = new HashMap<>();
        for (HostElement host: hosts) {
            usages.put(host, host.getMem_usage());
        }
        return usages;
    }
    
    public Map<HostElement, Integer> initializeDiskCapacity(List<HostElement> hosts) {
        Map<HostElement, Integer> usages = new HashMap<>();
        for (HostElement h: hosts) {
            usages.put(h, h.getFree_disk());
        }
        return usages;
    }
    
    public Map<HostElement, Float> addCpuCapacity(HostElement host, VmElement vm, Map<HostElement, Float> usages) {
        usages.replace(host, usages.get(host), usages.get(host) + vm.getCpu());
        return usages;
    }
    
    public Map<HostElement, Integer> addMemoryCapacity(HostElement host, VmElement vm, Map<HostElement, Integer> usages) {
        usages.replace(host, usages.get(host), usages.get(host) + vm.getMemory());
        return usages;
    }
    
    public Map<HostElement, Integer> addDiskCapacity(HostElement host, VmElement vm, Map<HostElement, Integer> usages) {
        usages.replace(host, usages.get(host), usages.get(host) - vm.getDiskSizes());
        return usages;
    }
    public Map<HostElement, Float> getCpuUsages() {
        return cpuUsages;
    }

    public Map<HostElement, Integer> getMemoryUsages() {
        return memoryUsages;
    }

    public Map<HostElement, Integer> getMemoryUsagesDs() {
        return diskUsages;
    }
    

}