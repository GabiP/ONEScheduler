package cz.muni.fi.main;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import cz.muni.fi.pools.Host;
import cz.muni.fi.pools.HostDS;
import cz.muni.fi.pools.Hostpool;
import cz.muni.fi.pools.User;
import cz.muni.fi.pools.Userpool;
import cz.muni.fi.pools.Vm;
import cz.muni.fi.pools.Vmpool;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.opennebula.client.Client;
import org.opennebula.client.OneResponse;
import org.opennebula.client.host.HostPool;
import org.opennebula.client.user.UserPool;
import org.opennebula.client.vm.VirtualMachinePool;
import scheduler.Scheduler;


/**
 * See http://docs.opennebula.org/4.12/integration/system_interfaces/api.html#schemas-for-host
 *
 * @author Gabriela Podolnikova
 */
public class Main {

    public static void main(String[] args) {
        
        /*Client oneClient;
        
        Hostpool hostpool = null;
        
        Vmpool vmpool = null;
        
        Userpool upool = null;
        
        XmlMapper xmlMapper = new XmlMapper();
        
        try {
            String SECRET = "oneadmin:opennebula";
            String ENDPOINT = "http://localhost:2633/RPC2";

            oneClient = new Client(SECRET, ENDPOINT);
            
            HostPool hp = new HostPool(oneClient);
            OneResponse hpr = hp.info();
            System.out.println(hpr.getMessage());
            hostpool = xmlMapper.readValue(hpr.getMessage(), Hostpool.class);
            
            VirtualMachinePool vmp = new VirtualMachinePool(oneClient);
            OneResponse vmpr = vmp.info();
            System.out.println(vmpr.getMessage());
            vmpool = xmlMapper.readValue(vmpr.getMessage(), Vmpool.class);
            
            UserPool up = new UserPool(oneClient);
            OneResponse upr = up.info();
            System.out.println(upr.getMessage());
            upool = xmlMapper.readValue(upr.getMessage(), Userpool.class);

            
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        System.out.println("hostpool = " + hostpool);

        Host host = hostpool.getHosts()[0];
        System.out.println("host = " + host);
        
        System.out.println("Host running vms: " + host.getId() + " " + host.getHostShare().getRunning_vms());

        System.out.println("vmpool = " + vmpool);

        Vm vm = vmpool.getVms()[0];
        System.out.println("vm = " + vm);
        
        System.out.println("upool = " + upool);

        User u = upool.getUsers()[0];
        System.out.println("u = " + u);*/
        
        Scheduler scheduler = new Scheduler();
        scheduler.init();
        try {
            scheduler.body();
        } catch (InterruptedException | IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
