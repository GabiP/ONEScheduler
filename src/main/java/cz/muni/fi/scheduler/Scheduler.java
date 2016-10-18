package cz.muni.fi.scheduler;

import cz.muni.fi.authorization.IAuthorizationManager;
import cz.muni.fi.scheduler.elementpools.IClusterPool;
import cz.muni.fi.scheduler.elementpools.IDatastorePool;
import cz.muni.fi.scheduler.elementpools.IHostPool;
import cz.muni.fi.scheduler.elementpools.IVmPool;
import cz.muni.fi.scheduler.fairshare.AbstractPriorityCalculator;
import cz.muni.fi.scheduler.fairshare.FairShareOrderer;
import cz.muni.fi.scheduler.filters.datastores.IDatastoreFilter;
import cz.muni.fi.scheduler.resources.DatastoreElement;
import cz.muni.fi.scheduler.resources.HostElement;
import cz.muni.fi.scheduler.resources.VmElement;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import cz.muni.fi.scheduler.policies.datastores.IStoragePolicy;
import cz.muni.fi.scheduler.policies.hosts.IPlacementPolicy;
import java.io.IOException;
import java.util.LinkedHashMap;
import cz.muni.fi.scheduler.filters.hosts.strategies.IHostFilterStrategy;
import cz.muni.fi.scheduler.filters.datastores.strategies.IDatastoreFilterStrategy;
import cz.muni.fi.scheduler.filters.hosts.IHostFilter;

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
        
    private IDatastorePool dsPool;
    
    /**
     * This attribute holds all important data for scheduling.
     * Needs to be updated every new cycle.
     */
    private SchedulerData schedulerData;
    
    /**
     * 
     */
    private IHostFilter hostFilter;
    
    /**
     * The list of filters to be used for matching the datastore for a virtual machine.
     */
    private IDatastoreFilter datastoreFilter;
    
    /**
     * The list of policies to be used for sorting the hosts available for a virtual machine.
     */
    private IPlacementPolicy placementPolicy;
    
    /**
     * The list of policies to be used for choosing the best ranked datastore available for a virtual machine and a host.
     */
    private IStoragePolicy storagePolicy;
    
    /**
     * The list of fairshare policies to be used for sorting the virtual machines based on their user's priority.
     */
    private FairShareOrderer fairshare;  
    
    /**
     * Queues with waiting VMs.
     * Note: Load number of queues from configuration file.
     * We will use list of LinkedLists for multiple queues.
     * For concurrent queues we can use: ConcurrentLinkedQueue
     */
    private List<LinkedList<VmElement>> queues;
    
    private int numberOfQueues;
    
    private boolean preferHostFit;

    public Scheduler(IAuthorizationManager authorizationManager, IHostPool hostPool, IVmPool vmPool, IDatastorePool dsPool, IHostFilter hostFilter, IDatastoreFilter datastoreFilter, IPlacementPolicy placementPolicy, IStoragePolicy storagePolicy, FairShareOrderer fairshare, int numberOfQueues, boolean preferHostFit) {
        this.authorizationManager = authorizationManager;
        this.hostPool = hostPool;
        this.vmPool = vmPool;
        this.dsPool = dsPool;
        this.hostFilter = hostFilter;
        this.datastoreFilter = datastoreFilter;
        this.placementPolicy = placementPolicy;
        this.storagePolicy = storagePolicy;
        this.fairshare = fairshare;
        this.numberOfQueues = numberOfQueues;
        this.preferHostFit = preferHostFit;
        //initialize scheduler data entity
        schedulerData = new SchedulerData(hostPool, vmPool, dsPool);
    }
    
    /**
     * This method starts the scheduling.
     * Gets active hosts, initialize capacity maps, gets pending virtual machines and puts in the queues.
     * Calls fairshare and calls the method to process queues.
     * @return the map with the plan
     */
    public List<Match> schedule() {
        //get pendings, state = 1 is pending
        List<VmElement> pendingVms = vmPool.getVmsByState(1);
        if (pendingVms.isEmpty()) {
            System.out.println("No pendings");
            return null;
        }
        queues = new LinkedList<>();
        //TODO sort VM in queues by fairshare - using number of queues parameter
        List<VmElement> orderedVms = fairshare.orderVms(pendingVms);
        // VM queue construction
        for (int i = 0; i < numberOfQueues; i++) {
            LinkedList<VmElement> queue = new LinkedList<>();
            queue.addAll(orderedVms);
            queues.add(queue);
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
    private List<Match> processQueue(LinkedList queue) {
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
            List<HostElement> filteredHosts = hostFilter.getFilteredHosts(authorizedHosts, vm);
            //TODO: sort by every chosen policy in threads
            List<HostElement> sortedHosts = placementPolicy.sortHosts(filteredHosts, vm, schedulerData);
            
            //TODO: threading for policies
            Map<HostElement, RankPair> sortedCandidates = sortCandidates(sortedHosts, vm, storagePolicy);

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
     * This method creates a map containing hosts sorted by the placement policy. It means that the
     * first host in this map suites the most current vm in the queue. Each host
     * has datastores that the vm can be put on, and also the datastores are
     * sorted by the storage policy. LinkedHashMap preserves order in which the
     * objects are entered.
     * @param sortedHosts list of hosts sorted by the placement policy
     * @param vm the current virtual machine int he queue
     * @return the map containing the hosts and datastores (candidates) suitable for vm and sorted by policies
     */
    private Map<HostElement, RankPair> sortCandidates(List<HostElement> sortedHosts, VmElement vm, IStoragePolicy storagePolicy) {
        Map<HostElement, RankPair> sortedCandidates = new LinkedHashMap<>();
        for (HostElement host : sortedHosts) {
            List<DatastoreElement> filteredDatastores = datastoreFilter.filterDatastores(dsPool.getSystemDs(), host, vm);
            if (!filteredDatastores.isEmpty()) {
                RankPair ds = storagePolicy.selectDatastore(filteredDatastores, host, schedulerData);
                sortedCandidates.put(host, ds);
            }
        }
        return sortedCandidates;
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
    
    
    
    

    

    
    
}