package scheduler;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import cz.muni.fi.pools.Host;
import cz.muni.fi.pools.Hostpool;
import cz.muni.fi.pools.User;
import cz.muni.fi.pools.Userpool;
import cz.muni.fi.pools.Vm;
import cz.muni.fi.pools.Vmpool;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;
import org.opennebula.client.Client;
import org.opennebula.client.OneResponse;
import org.opennebula.client.host.HostPool;
import org.opennebula.client.user.UserPool;
import org.opennebula.client.vm.VirtualMachinePool;
import org.opennebula.client.vm.VirtualMachine;

/**
 *
 * @author Gabriela Podolnikova
 */
public class Scheduler {
    
    Client oneClient;
    
    XmlMapper xmlMapper = new XmlMapper();
    
    private Hostpool hostpool = null;
        
    private Vmpool vmpool = null;
    
    private Userpool userpool = null;
    
    private Host[] hosts;
    
    private Vm[] vms;
    
    private User[] users;
    
    private ArrayList<Host> filteredHosts = new ArrayList<>();
    
    private ArrayList<Vm> pendingVms = new ArrayList<>();;
    
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
            hosts = hostpool.getHosts();
            // Load VMs
            loadVms();
            vms = vmpool.getVms();
            // get only pending VMs
            pendingVms = getPendingVms(vms);
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
              Vm vm = (Vm) queue.peek();
              // check limits
              // filter hosts - whether the vm can be hosted - testCapacity...
              for (Host h: hosts) {
                  boolean canBeHosted  = h.testCapacity(vm);
                  if (canBeHosted) {
                      filteredHosts.add(h);
                  }
              }
              // deploy if filtered hosts is not null
              if (!filteredHosts.isEmpty()) {
                  VirtualMachine vmOne = new VirtualMachine(vm.getVmId(), oneClient);
                  vmOne.deploy(filteredHosts.get(0).getId());
                  System.out.println("Deploying vm: " + vm.getVmId() + "on host: " + filteredHosts.get(0).getId());
                // addCapacity - is this necessary?
                // poll VM from queue
                queue.poll();
              }
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
        System.out.println(hpr.getMessage());
        hostpool = xmlMapper.readValue(hpr.getMessage(), Hostpool.class);
    }
    
    public void loadVms() throws IOException {
        VirtualMachinePool vmp = new VirtualMachinePool(oneClient);
        OneResponse vmpr = vmp.info();
        System.out.println(vmpr.getMessage());
        vmpool = xmlMapper.readValue(vmpr.getMessage(), Vmpool.class);
    }
    
    public void loadUsers() throws IOException {
        UserPool up = new UserPool(oneClient);
        OneResponse upr = up.info();
        System.out.println(upr.getMessage());
        userpool = xmlMapper.readValue(upr.getMessage(), Userpool.class);
    }
    
    public ArrayList<Vm> getPendingVms(Vm[] vms) {
        for (Vm vm: vms) {
            if (vm.getState() == 1) {
                pendingVms.add(vm);
            }
        }
        return pendingVms;
    }

}
