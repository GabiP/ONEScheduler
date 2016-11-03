package cz.muni.fi.scheduler;

import cz.muni.fi.config.RecordManagerConfig;
import cz.muni.fi.config.SchedulerConfig;
import cz.muni.fi.scheduler.fairshare.historyrecords.IUserFairshareRecordManager;
import cz.muni.fi.scheduler.fairshare.historyrecords.UserFairshareRecordManager;
import cz.muni.fi.scheduler.fairshare.historyrecords.VmFairshareRecordManager;
import cz.muni.fi.scheduler.resources.VmElement;
import java.io.IOException;
import java.util.Date;
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
    private static boolean useXml;
    
    protected static final Logger LOG = LoggerFactory.getLogger(SetUp.class);
    
    public static void main(String[] args) throws InterruptedException {
        try {
            configuration = new PropertiesConfig("configuration.properties");
        } catch (IOException e) {
            LOG.error("Could not load configuration file!" + e);
            return;
        }
        
        cycleinterval = configuration.getInt("cycleinterval");
        useXml = configuration.getBoolean("useXml");
        
        if (useXml) {
            clearFairshareRecords();
        } 
                
        while (true) {
            LOG.info("Starting scheduling cycle.");     
            
            ApplicationContext context = new AnnotationConfigApplicationContext(SchedulerConfig.class);
            saveSchedulingTime();
            checkDecayTime(context.getBean(IUserFairshareRecordManager.class));
            Scheduler scheduler = context.getBean(Scheduler.class);
            List<List<Match>> plan = scheduler.schedule();
            printPlan(plan);
            
            LOG.info("Another cycle will start in " + cycleinterval + "seconds.");
            TimeUnit.SECONDS.sleep(cycleinterval);
        }
    }
    
    private static void clearFairshareRecords() {
        ApplicationContext context = new AnnotationConfigApplicationContext(RecordManagerConfig.class); 
        context.getBean(VmFairshareRecordManager.class).clearContent();
        context.getBean(UserFairshareRecordManager.class).clearContent();
    }

    private static void saveSchedulingTime() {
        if (useXml) {            
            // TODO: add date from Dalibor
            TimeManager.getInstance().setSchedulingTimeStamp(new Date());
        } else {
            TimeManager.getInstance().setSchedulingTimeStamp(new Date());
        }
    }
    
    private static void checkDecayTime(IUserFairshareRecordManager userRecordManager) {
        long schedulingTime = TimeManager.getInstance().getSchedulingTimeStamp().getTime();
        long lastDecayTime = userRecordManager.getLastDecayTime();
        long decayMillisInterval = TimeUnit.DAYS.toMillis(configuration.getInt("decayDayInterval"));
        
        if (schedulingTime - lastDecayTime > decayMillisInterval) {
            int decayValue = configuration.getInt("decayValue");
            userRecordManager.applyDecay(decayValue);
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
