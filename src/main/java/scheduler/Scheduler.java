package scheduler;

import cz.muni.fi.pools.ClusterXml;
import cz.muni.fi.pools.HostXml;
import cz.muni.fi.pools.UserXml;
import cz.muni.fi.pools.VmXml;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.opennebula.client.Client;
import org.opennebula.client.OneResponse;
import org.opennebula.client.acl.Acl;
import org.opennebula.client.acl.AclPool;
import org.opennebula.client.acl.RuleParseException;
import org.opennebula.client.cluster.Cluster;
import org.opennebula.client.cluster.ClusterPool;
import org.opennebula.client.host.Host;
import org.opennebula.client.host.HostPool;
import org.opennebula.client.user.User;
import org.opennebula.client.user.UserPool;
import org.opennebula.client.vm.VirtualMachinePool;
import org.opennebula.client.vm.VirtualMachine;

/**
 *
 * @author Gabriela Podolnikova
 */
public class Scheduler {
    
    Client oneClient;   
    
    private ArrayList<HostXml> hosts = new ArrayList<>();
    
    private ArrayList<UserXml> users = new ArrayList<>();
    
    private ArrayList<ClusterXml> clusters = new ArrayList<>();
    
    private ArrayList<HostXml> filteredHosts = new ArrayList<>();
    
    private ArrayList<VmXml> pendingVms = new ArrayList<>();
    
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
            String ENDPOINT = "http://localhost:2633/RPC2";

            oneClient = new Client(SECRET, ENDPOINT);
            
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    
    public void body() throws InterruptedException, IOException {
        while(true) {
            // Load Hosts
            loadHosts();
            // Load VMs
            loadVms();
            
            loadClusters();
            
            loadAcls();
            
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
                  System.out.println(h);
                  
                  boolean canBeHosted  = h.testCapacity(vm);
                  if (canBeHosted) {
                      filteredHosts.add(h);
                  }
              }
              // deploy if filtered hosts is not null
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
          System.out.println("New cycle will start in 60 seconds");
          TimeUnit.SECONDS.sleep(60);
        }
    }
    
    public void loadHosts() throws IOException {
        HostPool hp = new HostPool(oneClient);
        OneResponse hpr = hp.info();
        if (hpr.isError()) {
            //TODO: log it
            System.out.println(hpr.getErrorMessage());
        }
        Iterator<Host> itr = hp.iterator();
        while (itr.hasNext()) {
            Host element = itr.next();
            System.out.println("Host: " + element + "   state: " + element.state());
            if (element.state() == 1 || element.state() == 2) {
                HostXml h = new HostXml(element);
                hosts.add(h);
            }
        };
    }
    
    public void loadVms() throws IOException {
        VirtualMachinePool vmp = new VirtualMachinePool(oneClient);
        OneResponse vmpr = vmp.info();
        if (vmpr.isError()) {
            //TODO: log it
            System.out.println(vmpr.getErrorMessage());
        }
        Iterator<VirtualMachine> itr = vmp.iterator();
        while (itr.hasNext()) {
            VirtualMachine element = itr.next();
            System.out.println("VirtualMachine: " + element + "   state: " + element.state() + "   lcm_state: " + element.lcmState() + " ");
            //getting pendings
            if (element.state() == 1 || element.state() == 2) {
                VmXml vm = new VmXml(element);
                System.out.println("Vm: " + vm);
                pendingVms.add(vm);
            }
        }
    }
    
    public void loadUsers() throws IOException {
        UserPool up = new UserPool(oneClient);
        OneResponse upr = up.info();
        if (upr.isError()) {
            //TODO: log it
            System.out.println(upr.getErrorMessage());
        }
        Iterator<User> itr = up.iterator();
        while (itr.hasNext()) {
            User element = itr.next();
            System.out.println("User: " + element);
            UserXml u = new UserXml(element);
            System.out.println("User: " + u);
            users.add(u);
        }
    }
    
    public void loadClusters() {
        ClusterPool cp = new ClusterPool(oneClient);
        OneResponse cpr = cp.info();
        if (cpr.isError()) {
            //TODO: log it
            System.out.println(cpr.getErrorMessage());
        }
        Iterator<Cluster> itr = cp.iterator();
        while (itr.hasNext()) {
            Cluster element = itr.next();
            System.out.println("Cluster: " + element);
            ClusterXml c = new ClusterXml(element);
            System.out.println("Cluster: " + c);
            clusters.add(c);
        }
    }
    
    public void loadAcls() {
        AclPool aclp = new AclPool(oneClient);
        OneResponse aclpr = aclp.info();
        if (aclpr.isError()) {
            //TODO: log it
            System.out.println(aclpr.getErrorMessage());
        }
        Iterator<Acl> itr = aclp.iterator();
        while (itr.hasNext()) {
            Acl el = itr.next();
            System.out.println("Acl rule number: " + el.getId() + " resources: " + el.resource() + " rights: " + el.rights() + " users: " + el.user() + " toString: " +el.toString());
            String[] parsedRule = null;
            try {
                parsedRule = Acl.parseRule(el.toString());
            } catch (RuleParseException ex) {
                Logger.getLogger(Scheduler.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println(parsedRule[0]);
            System.out.println(parsedRule[1]);
            System.out.println(parsedRule[2]);
            acls.add(el);
        }
        
    }
    
    public ArrayList<VmXml> getPendingVms(VmXml[] vms) {
        for (VmXml vm: vms) {
            if (vm.getState() == 1 || vm.getState() == 2) {
                pendingVms.add(vm);
            }
        }
        return pendingVms;
    }
}
