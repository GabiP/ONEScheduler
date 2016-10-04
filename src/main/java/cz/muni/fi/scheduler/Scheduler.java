package cz.muni.fi.scheduler;

import cz.muni.fi.authorization.IAuthorizationManager;
import cz.muni.fi.scheduler.elementpools.IClusterPool;
import cz.muni.fi.scheduler.elementpools.IDatastorePool;
import cz.muni.fi.scheduler.elementpools.IHostPool;
import cz.muni.fi.scheduler.elementpools.IVmPool;
import cz.muni.fi.scheduler.fairshare.AbstractPriorityCalculator;
import cz.muni.fi.scheduler.filters.datastores.IDatastoreFilter;
import cz.muni.fi.scheduler.resources.DatastoreElement;
import cz.muni.fi.scheduler.resources.HostElement;
import cz.muni.fi.scheduler.resources.VmElement;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import cz.muni.fi.scheduler.filters.hosts.IHostFilter;
import cz.muni.fi.scheduler.policies.datastores.IStoragePolicy;
import cz.muni.fi.scheduler.policies.hosts.IPlacementPolicy;
import java.io.IOException;
import java.util.LinkedHashMap;

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
    private List<AbstractPriorityCalculator> listFairshare;  
    
    /**
     * Queues with waiting VMs.
     * Note: Load number of queues from configuration file.
     * We will use list of LinkedLists for multiple queues.
     * For concurrent queues we can use: ConcurrentLinkedQueue
     */
    private List<LinkedList<VmElement>> queues;
    
    private int numberOfQueues;
    
    private boolean preferHostFit;

    Scheduler(IManager manager, IResultManager resultManager, List<IHostFilter> hostFilters, List<IDatastoreFilter> datastoreFilters, List<IPlacementPolicy> listPlacementPolicies, List<IStoragePolicy> listStoragePolicy, List<AbstractPriorityCalculator> listFairshare, int numberOfQueues, boolean preferHostFit) throws IOException {
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
        this.numberOfQueues = numberOfQueues;
        this.preferHostFit = preferHostFit;
    }
    
    /**
     * This method starts the scheduling.
     * Gets active hosts, initialize capacity maps, gets pending virtual machines and puts in the queues.
     * Calls fairshare and calls the method to process queues.
     * @return the map with the plan
     */
    public List<Match> schedule() {
        //initialize scheduler data entity
        schedulerData = new SchedulerData(hostPool, vmPool, clusterPool, dsPool);
        //get pendings, state = 1 is pending
        List<VmElement> pendingVms = vmPool.getVmsByState(1);
        if (pendingVms.isEmpty()) {
            System.out.println("No pendings");
            return null;
        }
        //TODO sort VM in queues by fairshare - using number of queues parameter
        // VM queue construction
        for (int i = 0; i < numberOfQueues; i++) {
            LinkedList<VmElement> queue = new LinkedList<>();
            queue.addAll(pendingVms);
        }
        //process queue by queue or in parallel
        List<Match> plan = processQueue(queues.get(0));
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
    public List<Match> processQueue(LinkedList queue) {
        List<Match> plan = new ArrayList<>();
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
            //TODO: sort by every chosen policy in threads
            IPlacementPolicy placementPolicy = listPlacementPolicies.get(0);
            List<HostElement> sortedHosts = placementPolicy.sortHosts(filteredHosts, vm, schedulerData);
            /**
             * This map contains hosts sorted by the placement policy.
             * It means that the first host in this map suites the most current vm in the queue.
             * Each host has datastores that the vm can be put on, and also the datastores are sorted by the storage policy.
             * LinkedHashMap preserves order in which the objects are entered.
             */
            Map<HostElement, RankPair> sortedCandidates = new LinkedHashMap<>();
            //filter datastores for vm and host
            for (HostElement host: sortedHosts) {
                List<DatastoreElement> filteredDatastores = filterDatastores(dsPool.getSystemDs(), host, vm);
                if (!filteredDatastores.isEmpty()) {
                    //TODO: pick in threads by every chosen policy
                    IStoragePolicy storagePolicy = listStoragePolicy.get(0);
                    RankPair ds = storagePolicy.selectDatastore(filteredDatastores, host, schedulerData);
                    sortedCandidates.put(host, ds);
                }
            }
            // deploy if filtered hosts is not empty
            if (!sortedCandidates.isEmpty()) {
                //pick the host and datastore. We chose the best host, or the best datastore.
                Match match = createMatch(sortedCandidates);
                plan = match.addVm(plan, vm);
                System.out.println("Scheduling vm: " + vm.getVmId() + " on host: " + match.getHost() + "and ds: " + match.getDatastore());
                // change capacities
                schedulerData.addHostCpuCapacity(match.getHost(), vm);
                schedulerData.addHostMemoryCapacity(match.getHost(), vm);
                schedulerData.addHostRunningVm(match.getHost());
                schedulerData.addDatastoreStorageCapacity(match.getDatastore(), vm);
                schedulerData.addDatastoreNodeStorageCapacity(match.getHost(), match.getHost().getDatastoreNode(match.getDatastore().getId()), vm);
            }
            queue.poll();
        }
        return plan;
    }
    
    /**
     * Create Match from sorted candidates Map.
     * preferHostFit == true --> we choose the best host from map (keys) -- first key = best ranked host
     * preferHostFit == false --> we choose the best datastore from map (values) -- pick the best ranked datastore (using policy)
     * @param sortedCandidates contains hosts with datastores
     * @return chosen match for vm
     */
    private Match createMatch(Map<HostElement, RankPair> sortedCandidates) {
        HostElement chosenHost;
        DatastoreElement chosenDs;
        if (preferHostFit) {
            Map.Entry<HostElement, RankPair> entry = sortedCandidates.entrySet().iterator().next();
            chosenHost = entry.getKey();
            chosenDs = entry.getValue().getDs();
        } else {
            //for each policy in threads pick the ds
            IStoragePolicy storagePolicy = listStoragePolicy.get(0);
            chosenDs = storagePolicy.getBestRankedDatastore(new ArrayList(sortedCandidates.values()));
            chosenHost = getFirstHostThatHasDs(sortedCandidates, chosenDs);
        }
        return new Match(chosenHost, chosenDs);
    }
    
    /**
     * Finds the key (desired host) to corresponding value (known/chosen datastore). 
     * @param candidates the map with sorted candidates
     * @param chosenDs the value
     * @return the key that corresponds to the value
     */
    private HostElement getFirstHostThatHasDs(Map<HostElement, RankPair> candidates, DatastoreElement chosenDs) {
        HostElement result = null;
        for(Map.Entry<HostElement, RankPair> entry: candidates.entrySet()) {
            if (entry.getValue().equals(chosenDs)) {
                result = entry.getKey();
            }
        }
        return result;
    }
    
    /**
     * Filters hosts that are authorized for the specified vm.
     * Calls the filters for each host.
     * @param authorizedHosts the hosts to be tested.
     * @param vm the virtual machine to be tested
     * @return the list of filtered hosts
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
    
    /**
     * Filter datastores that belongs to the host and can host the vm.
     * @param datastores all system datastores in the system
     * @param host the host for matching the datastore
     * @param vm the vm to be tested
     * @return the list of filtered datastores
     */
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