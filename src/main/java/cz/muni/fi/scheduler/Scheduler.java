package cz.muni.fi.scheduler;

import cz.muni.fi.authorization.IAuthorizationManager;
import cz.muni.fi.scheduler.elementpools.IDatastorePool;
import cz.muni.fi.scheduler.elementpools.IHostPool;
import cz.muni.fi.scheduler.elementpools.IVmPool;
import cz.muni.fi.scheduler.fairshare.FairShareOrderer;
import cz.muni.fi.scheduler.filters.datastores.SchedulingDatastoreFilter;
import cz.muni.fi.scheduler.filters.hosts.SchedulingHostFilter;
import cz.muni.fi.scheduler.resources.DatastoreElement;
import cz.muni.fi.scheduler.resources.HostElement;
import cz.muni.fi.scheduler.resources.VmElement;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import cz.muni.fi.scheduler.policies.datastores.IStoragePolicy;
import cz.muni.fi.scheduler.policies.hosts.IPlacementPolicy;
import java.util.LinkedHashMap;
import org.apache.commons.collections4.ListUtils;

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
     * SchedulerData holds data that are updated everytime a vm is placed on a host.
     * Needs to be cleared every new cycle (after the plan is executed).
     */
    private SchedulerData schedulerData;
    
    /**
     * The hostFilter is used for filtering hosts. The filters that are used depends on configuration.
     */
    private SchedulingHostFilter hostFilter;
    
    /**
     * The datastoreFilter is used for filtering datastores. The filters that are used depends on configuration.
     */
    private SchedulingDatastoreFilter datastoreFilter;
    
    /**
     * The policy to be used for sorting the hosts available for a virtual machine.
     */
    private IPlacementPolicy placementPolicy;
    
    /**
     * The policy to be used for choosing the best ranked datastore available for a virtual machine and a host.
     */
    private IStoragePolicy storagePolicy;
    
    /**
     * The fairshare policy to be used for sorting the virtual machines based on their user's priority.
     */
    private FairShareOrderer fairshare;  
    
    /**
     * Queues with waiting VMs.
     * Note: Load number of queues from configuration file.
     * We will use list of LinkedLists for multiple queues.
     * For concurrent queues we can use: ConcurrentLinkedQueue
     */
    private List<List<VmElement>> queues;
    
    private int numberOfQueues;
    
    private boolean preferHostFit;

    public Scheduler(IAuthorizationManager authorizationManager, IHostPool hostPool, IVmPool vmPool, IDatastorePool dsPool, SchedulingHostFilter hostFilter, SchedulingDatastoreFilter datastoreFilter, IPlacementPolicy placementPolicy, IStoragePolicy storagePolicy, FairShareOrderer fairshare, int numberOfQueues, boolean preferHostFit) {
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
    public List<List<Match>> schedule() {
        //get pendings, state = 1 is pending
        List<VmElement> pendingVms = vmPool.getVmsByState(1);
        if (pendingVms.isEmpty()) {
            System.out.println("No pendings");
            return null;
        }
        System.out.println("Datastores: " + dsPool.getDatastores());
        //get list of vms ordered by fairshare
        List<VmElement> orderedVms = fairshare.orderVms(pendingVms);
        // VM queues construction
        queues = initializeQueues(numberOfQueues, orderedVms);
        //process queue by queue and create list of plans for each queues
        List<List<Match>> result = new LinkedList<>();
        for (List<VmElement> queue: queues) {
            List<Match> plan = processQueue(queue);
            result.add(plan);
        }
        return result;
    }
    
    private List<List<VmElement>> initializeQueues(int numberOfQueues, List<VmElement> orderedVms) {
        int numberOfVmsInQueue = (int) Math.ceil(orderedVms.size()/ new Float(numberOfQueues));
        List<List<VmElement>> output = ListUtils.partition(orderedVms, numberOfVmsInQueue);
        return output;
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
    private List<Match> processQueue(List<VmElement> queue) {
        List<Match> plan = new ArrayList<>();
        List<HostElement> authorizedHosts;
        for(VmElement vm: queue) {
            //check the authorization for this VM
            authorizedHosts = authorizationManager.authorize(vm);
            if (authorizedHosts.isEmpty()) {
                System.out.println("Empty authorized hosts.");
                continue;
            }
            //filter authorized hosts for vm
            List<HostElement> filteredHosts = hostFilter.getFilteredHosts(authorizedHosts, vm, schedulerData);
            //sort hosts
            List<HostElement> sortedHosts = placementPolicy.sortHosts(filteredHosts, vm, schedulerData);
            //filter and sort datastores for hosts
            Map<HostElement, RankPair> sortedCandidates = sortCandidates(sortedHosts, vm, storagePolicy);

            // deploy if sortedCandidates is not empty
            if (!sortedCandidates.isEmpty()) {
                //pick the host and datastore. We chose the best host, or the best datastore.
                Match match = createMatch(sortedCandidates);
                plan = match.addVm(plan, vm);
                System.out.println("Scheduling vm: " + vm.getVmId() + " on host: " + match.getHost() + "and ds: " + match.getDatastore());
                // update reservations
                schedulerData.reserveHostCpuCapacity(match.getHost(), vm);
                schedulerData.reserveHostMemoryCapacity(match.getHost(), vm);
                schedulerData.reserveHostRunningVm(match.getHost());
                schedulerData.reserveDatastoreStorage(match.getDatastore(), vm);
            }
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
            List<DatastoreElement> filteredDatastores = datastoreFilter.filterDatastores(dsPool.getSystemDs(), host, vm, schedulerData);
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