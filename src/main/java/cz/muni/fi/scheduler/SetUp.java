package cz.muni.fi.scheduler;

import cz.muni.fi.scheduler.resources.VmElement;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

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
    
    private static PropertiesConfig configuration;
    
    private static int cycleinterval;
    
    protected static final Logger LOG = LoggerFactory.getLogger(SetUp.class);
    
    public static void main(String[] args) throws InterruptedException {
        try {
            configuration = new PropertiesConfig("configuration.properties");
        } catch (IOException e) {
            LOG.error("Could not load configuration file!" + e);
            return;
        }
        
        cycleinterval = configuration.getInt("cycleinterval");
        
        ApplicationContext context = new AnnotationConfigApplicationContext(BeanConfig.class);
                
        while (true) {
            LOG.info("Starting scheduling cycle.");
            Scheduler scheduler = context.getBean(Scheduler.class);
            List<List<Match>> plan = scheduler.schedule();
            printPlan(plan);
            LOG.info("Another cycle will start in " + cycleinterval + "seconds.");
            TimeUnit.SECONDS.sleep(cycleinterval);
        }
    }
    
    private static void printPlan(List<List<Match>> plan ) {
        if (plan == null) {
            LOG.info("No schedule.");
            return;
        }
        int i = 0;
        for (List<Match> queue: plan) {
            System.out.println("Queue number: " + i);
            for (Match match : queue) {
                System.out.println("Host: " + match.getHost().getId());
                System.out.println("Its vms: ");
                for (VmElement vm : match.getVms()) {
                    System.out.println(vm);
                }
                System.out.println();
            }
            i++;
        }
    }

    
}
