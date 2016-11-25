package cz.muni.fi.scheduler.core;

import cz.muni.fi.authorization.IAuthorizationManager;
import cz.muni.fi.scheduler.elementpools.IDatastorePool;
import cz.muni.fi.scheduler.elementpools.IHostPool;
import cz.muni.fi.scheduler.elementpools.IVmPool;
import cz.muni.fi.scheduler.fairshare.FairShareOrderer;
import cz.muni.fi.scheduler.filters.datastores.SchedulingDatastoreFilter;
import cz.muni.fi.scheduler.filters.hosts.SchedulingHostFilter;
import cz.muni.fi.scheduler.limits.LimitChecker;
import cz.muni.fi.scheduler.resources.DatastoreElement;
import cz.muni.fi.scheduler.resources.HostElement;
import cz.muni.fi.scheduler.resources.VmElement;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import cz.muni.fi.scheduler.policies.datastores.IStoragePolicy;
import cz.muni.fi.scheduler.policies.hosts.IPlacementPolicy;
import cz.muni.fi.scheduler.queues.Queue;
import cz.muni.fi.scheduler.queues.QueueMapper;
import cz.muni.fi.scheduler.queues.QueuesExtensions;
import cz.muni.fi.scheduler.select.VmSelector;
import java.util.LinkedHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The class Scheduler is the core class responsible for all events during the scheduling.
 * Takes all pools in the system, the instance of AuthorizationManager and
 * information from configuration - Filters that will be used to filter hosts.
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
     * Mapping VMs into queues.
     */
    private QueueMapper queueMapper;
    
    /**
     * Selects VM to be scheduled.
     */
    private VmSelector vmSelector;
    
    /**
     * Checks resources limits for a user.
     */
    private LimitChecker limitChecker;
    
    /**
     * Queues with waiting VMs.
     * Note: Load number of queues from configuration file.
     * We will use list of LinkedLists for multiple queues.
     * For concurrent queues we can use: ConcurrentLinkedQueue
     */
    private List<Queue> queues;
    
    private int numberOfQueues;
    
    private boolean preferHostFit;
    
    protected final Logger LOG = LoggerFactory.getLogger(getClass());

    public Scheduler(IAuthorizationManager authorizationManager, IHostPool hostPool,
            IVmPool vmPool, IDatastorePool dsPool, SchedulingHostFilter hostFilter,
            SchedulingDatastoreFilter datastoreFilter, IPlacementPolicy placementPolicy,
            IStoragePolicy storagePolicy, FairShareOrderer fairshare, int numberOfQueues,
            boolean preferHostFit, QueueMapper queueMapper, VmSelector vmSelector, LimitChecker limitChecker) {
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
        this.queueMapper = queueMapper;
        this.vmSelector = vmSelector;
        this.limitChecker = limitChecker;
        //initialize scheduler data entity
        schedulerData = new SchedulerData();
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
            LOG.info("No pendings");
            return null;
        }
        //get list of vms ordered by fairshare
        List<VmElement> orderedVms = fairshare.orderVms(pendingVms);
        // VM queues construction
        queues = queueMapper.mapQueues(orderedVms);
        //process queue by queue and create list of plans for each queues
        List<List<Match>> result = new LinkedList<>();
        /*for (List<VmElement> queue: queues) {
            List<Match> plan = processQueue(queue);
            result.add(plan);
        }*/
        List<Match> processed = processQueues(queues);
        result.add(processed);
        return result;
    }
    
    private List<Match> processQueues(List<Queue> queues) {
        List<Match> plan = new ArrayList<>();
        while (!queues.isEmpty()) {
            VmElement vmSelected = vmSelector.selectVM(queues);
            Match match = processVm(vmSelected);
            if (match != null) {
                limitChecker.checkLimit(vmSelected, match);
                plan = match.addVm(plan, vmSelected);
            }
            //remove vmSelected from List<Queue>
            //if Queue is empty: remove Queue from list
            queues = QueuesExtensions.deleteVm(queues, vmSelected);
        }
        return plan;
    }

    /**
     * This method process the chosen vm.
     * And does this:
     * - retrieves the authorized hosts
     * - filter hosts by specified filters in the configuration file
     * --> it creates a subset of hosts that are suitable for the virtual machine.
     * - filter and sorts datastores for the hosts
     * Calls scheduling policy to select the host and the ds
     * If there is match for this VM, it returns it.
     * @param queue the queue to be processed
     * @return the macth for the vm
     */
    private Match processVm(VmElement vm) {
        Match match = null;
        List<HostElement> authorizedHosts = authorizationManager.getAuthorizedHosts();
        if (authorizedHosts.isEmpty()) {
            LOG.info("Empty authorized hosts.");
            return null;
        }
        //filter authorized hosts for vm 
        List<HostElement> filteredHosts = hostFilter.getFilteredHosts(authorizedHosts, vm, schedulerData);
        //sort hosts
        List<HostElement> sortedHosts = placementPolicy.sortHosts(filteredHosts, schedulerData);
        //filter and sort datastores for hosts
        Map<HostElement, RankPair> sortedCandidates = sortCandidates(sortedHosts, vm, storagePolicy);
        // deploy if sortedCandidates is not empty
        if (!sortedCandidates.isEmpty()) {
            //pick the host and datastore. We chose the best host, or the best datastore.
            match = createMatch(sortedCandidates);
            LOG.info("Scheduling vm: " + vm.getVmId() + " on host: " + match.getHost().getId() + " and ds: " + match.getDatastore().getId());
            // update reservations
            updateCachedData(match, vm);
        }
        return match;
    }
    
    private void updateCachedData(Match match, VmElement vm) {
        schedulerData.reserveHostCpuCapacity(match.getHost(), vm);
        schedulerData.reserveHostMemoryCapacity(match.getHost(), vm);
        schedulerData.reserveHostRunningVm(match.getHost());
        schedulerData.reserveDatastoreStorage(match.getDatastore(), vm);
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
        LOG.info("Created match host: " + chosenHost.getId() + " and datastore " + chosenDs.getId());
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