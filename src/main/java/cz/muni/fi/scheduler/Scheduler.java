package cz.muni.fi.scheduler;

import authorization.AuthorizationManager;
import cz.muni.fi.pools.AclXmlPool;
import cz.muni.fi.resources.ClusterXml;
import cz.muni.fi.pools.ClusterXmlPool;
import cz.muni.fi.pools.DatastoreXmlPool;
import cz.muni.fi.pools.HostXmlPool;
import cz.muni.fi.pools.TemplateXmlPool;
import cz.muni.fi.pools.UserXmlPool;
import cz.muni.fi.pools.VmXmlPool;
import cz.muni.fi.resources.DatastoreXml;
import cz.muni.fi.resources.HostXml;
import cz.muni.fi.resources.TemplateXml;
import cz.muni.fi.resources.UserXml;
import cz.muni.fi.resources.VmXml;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;
import org.opennebula.client.Client;
import org.opennebula.client.acl.Acl;

/**
 *
 * @author Gabriela Podolnikova
 */
public class Scheduler {
    
    Client oneClient;
    
    AuthorizationManager authorizationManager;
    
    HostXmlPool hostPool;
    
    VmXmlPool vmPool;
    
    UserXmlPool userPool;
    
    TemplateXmlPool templatePool;
    
    AclXmlPool aclPool;
    
    ClusterXmlPool clusterPool;
    
    DatastoreXmlPool dsPool;
    
    private ArrayList<HostXml> filteredHosts = new ArrayList<>();
    
    private ArrayList<VmXml> pendingVms = new ArrayList<>();  
    
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
        while(true) {
            //load all pools - must be the first call
            loadPools();
            //instantiate the Authorizationmanager
            authorizationManager = new AuthorizationManager(aclPool, clusterPool, hostPool, dsPool, userPool);
            //get pendings, state = 1 is pending
            pendingVms = vmPool.getVmsByState(1);           
            if (pendingVms.isEmpty()) {
                System.out.println("No pendings");
                break;
            }
            // Load configuration file (how many queues, priorities, quotas)
            // VM queue construction
            queue.addAll(pendingVms);
            // Criteria-based ordering
            // Run several algorithms. Write the results. Compare. Choose the best --> criteria. Then deploy in hostId.
            while(!queue.isEmpty()) {
              VmXml vm = (VmXml) queue.peek();
              System.out.println("Checking for vm: " + vm.getVmId());
              //check the authorization for this VM
              ArrayList<Integer> authorizedHosts = authorizationManager.authorize(vm);
              if (authorizedHosts.isEmpty()) {
                  System.out.println("Empty authorized hosts.");
              }
              // check limits
              // filter hosts - whether the vm can be hosted - testCapacity...
              for (Integer hostId: authorizedHosts) {
                  System.out.println("Host id " + hostId);
                  HostXml h = hostPool.getById(hostId);
                  boolean enoughCapacity  = h.testCapacity(vm);
                  if (enoughCapacity ==  false) {
                      System.out.println("Host does not have enough capacity - CPU, MEM to host the vm.");
                  }
                  boolean enoughCapacityDs = h.testDs(vm, clusterPool, dsPool);
                  if (enoughCapacityDs ==  false) {
                      System.out.println("Host does not have enough capacity in DATASTORES to host the vm.");
                  }
                  boolean reqs = vm.evaluateSchedReqs(h);
                  if (reqs ==  false) {
                      System.out.println("Host does not satisfy the scheduling requirements.");
                  }
                  boolean pciFits = h.checkPci(vm);
                  if (reqs ==  false) {
                      System.out.println("Host does not have the specied PCI.");
                  }
                  if (enoughCapacity && reqs && enoughCapacityDs && pciFits) {
                      filteredHosts.add(h);
                  }
              }
              // deploy if filtered hosts is not empty
              if (!filteredHosts.isEmpty()) {
                  //we wont be deploying here. Just 
                  vm.getVm().deploy(filteredHosts.get(0).getId());
                  filteredHosts.get(0).addCapacity(vm);
                  System.out.println("Deploying vm: " + vm.getVmId() + " on host: " + filteredHosts.get(0).getId());
                // addCapacity - id I add capacity, won't there be a problem with the parameters when it is actually deployed?
                // Should I make a copy of this parameters and ask for that parameter. And When I update the pools I also update the copy.
                // poll VM from queue
                // Will I be polling? - I should maybe use LinkedList and iterate through it. The poll queue deletes the value. I will be needing that in threads. 
              }
              queue.poll();
            }
          System.out.println("New cycle will start in 10 seconds");
          TimeUnit.SECONDS.sleep(10);
        }
    }
    
    public void loadPools() throws IOException {
        vmPool = new VmXmlPool(oneClient);
        hostPool = new HostXmlPool(oneClient);
        userPool = new UserXmlPool(oneClient);
        templatePool = new TemplateXmlPool(oneClient);
        aclPool = new AclXmlPool(oneClient);
        clusterPool = new ClusterXmlPool(oneClient);
        dsPool = new DatastoreXmlPool(oneClient);

        hostPool.loadHosts();
        vmPool.getAllVms();
        userPool.loadUsers();       
        aclPool.loadAcl();
        clusterPool.loadClusters();   
        dsPool.loadDatastores();
        templatePool.loadTemplates();
    }
}
