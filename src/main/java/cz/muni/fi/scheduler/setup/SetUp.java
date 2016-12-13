package cz.muni.fi.scheduler.setup;

import cz.muni.fi.scheduler.core.TimeManager;
import cz.muni.fi.scheduler.core.Match;
import cz.muni.fi.scheduler.core.Scheduler;
import cz.muni.fi.config.RecordManagerConfig;
import cz.muni.fi.config.SchedulerConfig;
import cz.muni.fi.result.IResultManager;
import cz.muni.fi.scheduler.fairshare.historyrecords.IUserFairshareRecordManager;
import cz.muni.fi.scheduler.fairshare.historyrecords.UserFairshareRecordManager;
import cz.muni.fi.scheduler.fairshare.historyrecords.VmFairshareRecordManager;
import cz.muni.fi.scheduler.elements.VmElement;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.opennebula.client.ClientConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * This class contains the main method.
 * Loads the configuration file and retreives its attributes.
 * Creates instance of a manager depending on whether we use OpenNebula or our own xml files.
 * Then creates an instance of a Scheduler and starts the scheduling.
 * 
 * @author Gabriela Podolnikova
 */
public class SetUp {
    
    private static PropertiesConfig configuration;
    private static PropertiesConfig fairshareConfig;
    
    private static int cycleinterval;
    private static boolean testingMode;
    
    private static final String DEFAULT_FILE_NAME = "configuration.properties";
    private static final String DEFAULT_FILE_NAME_FAIRSHARE = "fairshare.properties";
    
    protected static final Logger log = LoggerFactory.getLogger(SetUp.class);
    
    public static void main(String[] args) throws InterruptedException, ClientConfigurationException {
        try {
            configuration = new PropertiesConfig(DEFAULT_FILE_NAME);
            fairshareConfig = new PropertiesConfig(DEFAULT_FILE_NAME_FAIRSHARE);
        } catch (IOException e) {
            log.error("Could not load configuration file!" + e);
            return;
        }
        
        cycleinterval = configuration.getInt("cycleinterval");
        testingMode = configuration.getBoolean("testingMode");
        
        if (testingMode) {
            log.info("Dalibor was here");
            clearFairshareRecords();
        } 
                
        while (true) {
            log.info("Starting scheduling cycle.");     
            
            ApplicationContext context = new AnnotationConfigApplicationContext(SchedulerConfig.class);
            
            saveSchedulingTime();
            checkDecayTime(context.getBean(IUserFairshareRecordManager.class));
            Scheduler scheduler = context.getBean(Scheduler.class);
            IResultManager resultManager = context.getBean(IResultManager.class);
            
            //Plan migrations
            List<Match> migrations = scheduler.migrate();
            printPlan(migrations);
            //migrate
            List<VmElement> failedMigrations = resultManager.migrate(migrations);
            printFailedVms(failedMigrations);
            
            //Plan pendings
            List<Match> plan = scheduler.schedule();
            printPlan(plan);
            
            //deploy
            List<VmElement> failedVms = resultManager.deployPlan(plan);
            printFailedVms(failedVms);
            
            log.info("Another cycle will start in " + cycleinterval + "seconds.");
            TimeUnit.SECONDS.sleep(cycleinterval);
        }
    }
    
    private static void clearFairshareRecords() {
        ApplicationContext context = new AnnotationConfigApplicationContext(RecordManagerConfig.class); 
        context.getBean(VmFairshareRecordManager.class).clearContent();
        context.getBean(UserFairshareRecordManager.class).clearContent();
    }

    private static void saveSchedulingTime() {
        // TODO: when Dalibor's XML generator is ready add date it provides for testingMode
        TimeManager.getInstance().setSchedulingTimeStamp(new Date());
    }
    
    private static void checkDecayTime(IUserFairshareRecordManager userRecordManager) {
        long schedulingTime = TimeManager.getInstance().getSchedulingTimeStamp().getTime();
        long lastDecayTime = userRecordManager.getLastDecayTime();
        long decayMillisInterval = TimeUnit.DAYS.toMillis(fairshareConfig.getInt("decayDayInterval"));
        
        if (schedulingTime - lastDecayTime > decayMillisInterval) {
            int decayValue = fairshareConfig.getInt("decayValue");
            userRecordManager.applyDecay(decayValue);
        }
    }
    
    private static void printPlan(List<Match> plan) {
        if (plan == null) {
            log.info("No schedule.");
            return;
        }
        System.out.println("Schedule:");
        for (Match match : plan) {
            System.out.println("Host: " + match.getHost().getId());
            System.out.println("Its vms: ");
            match.getVms().forEach(System.out::println);
            System.out.println();
        }
    }
    
    private static void printFailedVms(List<VmElement> failedVms) {
        System.out.println("Failed Vms: ");
        failedVms.forEach(System.out::println);
    }
}
