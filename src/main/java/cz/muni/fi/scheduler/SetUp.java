package cz.muni.fi.scheduler;

import cz.muni.fi.scheduler.resources.VmElement;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * This class contains the main method.
 * Loads the configuration file and retreives its attributes.
 * Creates instance of a manager depending on whether we use OpenNebula or our own xml files.
 * Then creates an instance of a Scheduler and starts the scheduling.
 * TODO: Storing the results.
 * 
 * @author Gabriela Podolnikova
 */
public class SetUp {
    
    private static Configuration configuration;
    
    private static int cycleinterval;
    
    public static void main(String[] args) throws InterruptedException {
        try {
            configuration = new Configuration("configuration.properties");
        } catch (IOException e) {
            System.err.println("Could not load configuration file!" + e);
            return;
        }
        
        cycleinterval = configuration.getInt("cycleinterval");
        
        ApplicationContext context = new ClassPathXmlApplicationContext("Beans.xml");
                
        while (true) {
            Scheduler scheduler = (Scheduler) context.getBean("scheduler");
            List<Match> plan = scheduler.schedule();
            printPlan(plan);
            TimeUnit.SECONDS.sleep(cycleinterval);
        }
    }
    
    private static void printPlan(List<Match> plan ) {
        if (plan == null) {
            System.out.println("No schedule.");
            return;
        }
        for (Match match : plan) {
            System.out.println(match.getHost());
            System.out.println("Its vms: ");
            for (VmElement vm : match.getVms()) {
                System.out.println(vm);
            }
            System.out.println();
        }
    }

    
}
