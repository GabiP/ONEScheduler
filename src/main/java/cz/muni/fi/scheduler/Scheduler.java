package cz.muni.fi.scheduler;

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
    
    HostXmlPool hostPool;
    
    VmXmlPool vmPool;
    
    UserXmlPool userPool;
    
    TemplateXmlPool templatePool;
    
    AclXmlPool aclPool;
    
    ClusterXmlPool clusterPool;
    
    DatastoreXmlPool dsPool;
    
    private ArrayList<HostXml> hosts = new ArrayList<>();
    
    private ArrayList<UserXml> users = new ArrayList<>();
    
    private ArrayList<TemplateXml> templates = new ArrayList<>();
    
    private ArrayList<ClusterXml> clusters = new ArrayList<>();
    
    private ArrayList<HostXml> filteredHosts = new ArrayList<>();
    
    private ArrayList<DatastoreXml> ds = new ArrayList<>();
    
    private ArrayList<VmXml> pendingVms = new ArrayList<>();
    
    private ArrayList<VmXml> vms = new ArrayList<>();
    
    private ArrayList<Acl> acls = new ArrayList<>();
    
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
            // Pass on the oneClient connection to pools
            vmPool = new VmXmlPool(oneClient);
            hostPool = new HostXmlPool(oneClient);
            userPool = new UserXmlPool(oneClient);
            templatePool = new TemplateXmlPool(oneClient);
            aclPool = new AclXmlPool(oneClient);
            clusterPool = new ClusterXmlPool(oneClient);
            dsPool = new DatastoreXmlPool(oneClient);
            
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    
    public void body() throws InterruptedException, IOException {
        while(true) {
            loadPools();
            //get pendings
            pendingVms = vmPool.getPendings();
            
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
              System.out.println(vm);
              // check limits
              // filter hosts - whether the vm can be hosted - testCapacity...
              for (HostXml h: hosts) {
                  System.out.println(h + " id " + h.getId());
                  
                  boolean canBeHosted  = h.testCapacity(vm);
                  boolean reqs = vm.evaluateSchedReqs(h);
                  if (canBeHosted && reqs) {
                      filteredHosts.add(h);
                  }
              }
              // deploy if filtered hosts is not empty
              if (!filteredHosts.isEmpty()) {
                  vm.getVm().deploy(filteredHosts.get(0).getId());
                  filteredHosts.get(0).addCapacity(vm);
                  System.out.println("Deploying vm: " + vm.getVmId() + " on host: " + filteredHosts.get(0).getId());
                // addCapacity - is this necessary?
                // poll VM from queue
              }
              queue.poll();
            }
            // flush Hosts
            // flush VMs
          System.out.println("New cycle will start in 10 seconds");
          TimeUnit.SECONDS.sleep(10);
        }
    }
    
    public void loadPools() throws IOException {
        // Load Hosts
        hosts = hostPool.loadHosts();
        // Load VMs
        vms = vmPool.loadVms();
        //Load users
        users = userPool.loadUsers();
        // Load acls       
        acls = aclPool.loadAcl();
        // Load clusters
        clusters = clusterPool.loadClusters();
        // Load datastores   
        ds = dsPool.loadDatastores();
        //Load templates
        templates = templatePool.loadTemplates();
    }
}
