package cz.muni.fi.scheduler;

import cz.muni.fi.authorization.IAuthorizationManager;
import cz.muni.fi.scheduler.elementpools.IClusterPool;
import cz.muni.fi.scheduler.elementpools.IDatastorePool;
import cz.muni.fi.scheduler.elementpools.IHostPool;
import cz.muni.fi.scheduler.elementpools.IVmPool;
import cz.muni.fi.scheduler.fairshare.FairshareFactory;
import cz.muni.fi.scheduler.fairshare.IUserPriorityCalculator;
import cz.muni.fi.scheduler.filters.datastores.IDatastoreFilter;
import cz.muni.fi.scheduler.resources.DatastoreElement;
import cz.muni.fi.scheduler.resources.HostElement;
import cz.muni.fi.scheduler.resources.VmElement;
import cz.muni.fi.scheduler.resources.nodes.DatastoreNode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import cz.muni.fi.scheduler.filters.hosts.IHostFilter;
import cz.muni.fi.scheduler.policies.PolicyFactory;
import cz.muni.fi.scheduler.policies.datastores.IStoragePolicy;
import cz.muni.fi.scheduler.policies.hosts.IPlacementPolicy;
import java.io.IOException;

/**
 * The class Scheduler is the core class responsible for all events during the scheduling.
 * Takes all pools in the system, the instance of AuthorizationManager and
 * infromation from configuration - Filters that will be used to filter hosts.
 * 
 * @author Gabriela Podolnikova
 */
public class Scheduler {
    
    private IAuthorizationManager authorizationManager;
    
    private IHostPool hostPool;
    
    private IVmPool vmPool;    
    
    private IClusterPool clusterPool;
    
    private IDatastorePool dsPool;
    
    private List<VmElement> pendingVms;
    
    private IResultManager resultManager;
    
    /**
     * This attribute holds all important data for scheduling.
     * Needs to be updated every new cycle.
     */
    private SchedulerData schedulerData;
    
    /**
     * The list of filters to be used for matching the host for a virtual machine.
     */
    private List<IHostFilter> hostFilters;
    
    /**
     * The list of filters to be used for matching the datastore for a virtual machine.
     */
    private List<IDatastoreFilter> datastoreFilters;
    
    /**
     * The list of policies to be used for sorting the hosts available for a virtual machine.
     */
    private List<IPlacementPolicy> listPlacementPolicies;
    
    /**
     * The list of policies to be used for choosing the best ranked datastore available for a virtual machine and a host.
     */
    private List<IStoragePolicy> listStoragePolicy;
    
    /**
     * The list of fairshare policies to be used for sorting the virtual machines based on their user's priority.
     */
    private List<IUserPriorityCalculator> listFairshare;
    
    
    /**
     * Queues with waiting VMs.
     * Note: Load number of queues from configuration file.
     * We will use list of LinkedLists for multiple queues.
     * For concurrent queues we can use: ConcurrentLinkedQueue
     */
    private LinkedList queue = new LinkedList();

    Scheduler(IManager manager, IResultManager resultManager, List<IHostFilter> hostFilters, List<IDatastoreFilter> datastoreFilters, List<IPlacementPolicy> listPlacementPolicies, List<IStoragePolicy> listStoragePolicy, List<IUserPriorityCalculator> listFairshare) throws IOException {
        this.vmPool = manager.getVmPool();
        this.hostPool = manager.getHostPool();
        this.clusterPool = manager.getClusterPool();
        this.dsPool = manager.getDatastorePool();
        this.authorizationManager = manager.getAuthorizationManager();
        this.resultManager = resultManager;
        this.hostFilters = hostFilters;
        this.datastoreFilters = datastoreFilters;
        this.listPlacementPolicies = listPlacementPolicies;
        this.listStoragePolicy = listStoragePolicy;
        this.listFairshare = listFairshare;
    }
    
    /**
     * This method starts the scheduling.
     * Gets active hosts, initialize capacity maps, gets pending virtual machines and puts in the queues.
     * Calls fairshare and calls the method to process queues.
     * @return the map with the plan
     */
    public Map<HostElement, List<VmElement>> schedule() {
        //initialize scheduler data entity
        schedulerData = new SchedulerData(hostPool, vmPool, clusterPool, dsPool);
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
            //TODO: sort filtered hosts based on chosen placement policy
            
            /**
             * This map contains hosts sorted by the placement policy.
             * It means that the first host in this map suites the most current vm in the queue.
             * Each host has datastores that the vm can be put on, and also the datastores are sorted by the storage policy.
             */
            Map<HostElement, List<DatastoreElement>> sortedCandidates = new HashMap<>();
            //filter datastores for vm and host
            for (HostElement host: filteredHosts) {
                List<DatastoreElement> filteredDatastores = filterDatastores(dsPool.getSystemDs(), host, vm);
                if (!filteredDatastores.isEmpty()) {
                    //TODO: filter datastores based on chosen storage policy
                            
                    sortedCandidates.put(host, filteredDatastores);
                }
            }
            // deploy if filtered hosts is not empty
            if (!sortedCandidates.isEmpty()) {
                //TODO: get from the sorted candidates map the host, that is the best - first or the one with most datastores
                HostElement chosenHost = filteredHosts.get(0);
                
                //Schedule the VM on chosen host
                plan = putValueToPlan(plan, chosenHost, vm);
                System.out.println("Scheduling vm: " + vm.getVmId() + " on host: " + filteredHosts.get(0).getId());
                // change capacities
                schedulerData.addHostCpuCapacity(chosenHost, vm);
                schedulerData.addHostMemoryCapacity(chosenHost, vm);
                schedulerData.addHostRunningVm(chosenHost);
                //TODO: update datastore usages
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
            boolean matched = getResultedHostsFromFilters(hostFilters, h, vm);
            if (matched) {
                filteredHosts.add(h);
            }
        }
        return filteredHosts;
    }
    
    public List<DatastoreElement> filterDatastores(List<DatastoreElement> datastores, HostElement host, VmElement vm) {
        List<DatastoreElement> filteredDatastores = new ArrayList<>();
        for (DatastoreElement ds: datastores) {
            boolean matched = getResultedDatastoresFromFilters(datastoreFilters, host, ds, vm);
            if (matched) {
                filteredDatastores.add(ds);
            }
        }
        return filteredDatastores;
    }
    
    public Map<HostElement, List<VmElement>> putValueToPlan(Map<HostElement, List<VmElement>> plan, HostElement host, VmElement vm) {
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
    public boolean getResultedHostsFromFilters(List<IHostFilter> filters, HostElement h, VmElement vm) {
         boolean result = true;
         for (IHostFilter filter: filters) {
             result = result && filter.test(vm, h, schedulerData);
         }
         return result;
     }
    
    /**
     * Goes through all filters and calls the test if the vm and host matches by the specified criteria in the filter.
     * @param filters filters to be used
     * @param host the host to be tested
     * @param ds the datasotre to be tested
     * @param vm the virtual machine to be tested
     * @return true if the host and vm match, false othewise
     */
    public boolean getResultedDatastoresFromFilters(List<IDatastoreFilter> filters, HostElement host, DatastoreElement ds, VmElement vm) {
         boolean result = true;
         for (IDatastoreFilter filter: filters) {
             result = result && filter.test(vm, ds, host, schedulerData);
         }
         return result;
     }
}