package cz.muni.fi.main;

import java.io.IOException;

import java.util.logging.Level;
import java.util.logging.Logger;
import cz.muni.fi.scheduler.Scheduler;
import cz.muni.fi.xml.pools.HostXmlPool;
import java.nio.file.Files;
import java.nio.file.Paths;


/**
 * See http://docs.opennebula.org/4.12/integration/system_interfaces/api.html#schemas-for-host
 *
 * @author Gabriela Podolnikova
 */
public class Main {

    public static void main(String[] args) throws IOException {        
        /*Scheduler scheduler = new Scheduler();
        scheduler.init();
        try {
            scheduler.body();
        } catch (InterruptedException | IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }*/
        String message = new String(Files.readAllBytes(Paths.get("example.xml")));
        HostXmlPool hosts = new HostXmlPool(message);
        System.out.println(hosts.getHosts());
               
    }
    
}
