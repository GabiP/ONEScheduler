package cz.muni.fi.config;

import cz.muni.fi.exceptions.LoadingFailedException;
import cz.muni.fi.scheduler.limits.LimitChecker;
import cz.muni.fi.scheduler.limits.QuotasCheck;
import cz.muni.fi.scheduler.queues.FairshareMapper;
import cz.muni.fi.scheduler.queues.FixedNumofQueuesMapper;
import cz.muni.fi.scheduler.queues.QueueByUser;
import cz.muni.fi.scheduler.queues.QueueMapper;
import cz.muni.fi.scheduler.queues.UserFairshareMapper;
import cz.muni.fi.scheduler.queues.UserGroupFairshareMapper;
import cz.muni.fi.scheduler.select.QueueByQueue;
import cz.muni.fi.scheduler.select.RoundRobin;
import cz.muni.fi.scheduler.select.VmSelector;
import cz.muni.fi.scheduler.setup.PropertiesConfig;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 *  Configures scheduler's behaviour.
 * 
 * @author Gabriela Podolnikova
 */
@Configuration
@Import({PoolConfig.class})
public class SchedulingConfig {
    
    @Autowired PoolConfig poolConfig;
    @Autowired FairshareConfig fairshareConfig;
    
    private PropertiesConfig properties;
    
    private static final String FAIRSHARE_MAPPER = "FairshareMapper";
    private static final String FIXED_NUM_OF_QUEUES = "FixedNumofQueuesMapper";
    private static final String QUEUE_BY_USER = "QueueByUser";
    private static final String USER_FAIRSHARE = "UserFairshare";
    private static final String USER_GROUP_FAIRSHARE = "UserGroupFairshare";
    
    private static final String QUEUE_BY_QUEUE = "QueueByQueue";   
    private static final String ROUND_ROBIN = "RoundRobin";
    
    private static final String QUOTAS_CHECK = "QuotasCheck";
    
    public SchedulingConfig() throws IOException {
        properties = new PropertiesConfig("configuration.properties");
    }
    
    @Bean
    public QueueMapper queueMapper() throws LoadingFailedException {
        switch (properties.getString("queueMapper")) {
            case FAIRSHARE_MAPPER:
                return new FairshareMapper();
            case FIXED_NUM_OF_QUEUES:
                return new FixedNumofQueuesMapper(properties.getInt("numberofqueues"));
            case QUEUE_BY_USER:
                return new QueueByUser(poolConfig.userPool());
            case USER_FAIRSHARE:
                return new UserFairshareMapper(fairshareConfig.userPriorityCalculator());
            case USER_GROUP_FAIRSHARE:
                return new UserGroupFairshareMapper(fairshareConfig.userPriorityCalculator(), poolConfig.userPool());
            default:
                throw new LoadingFailedException("Wrong queue mapper configuration.");
        }
    }
    
    @Bean
    public VmSelector vmSelector() throws LoadingFailedException {
        switch (properties.getString("vmSelector")) {
            case QUEUE_BY_QUEUE:
                return new QueueByQueue();
            case ROUND_ROBIN:
                return new RoundRobin();
            default:
                throw new LoadingFailedException("Wrong queue mapper configuration.");
        }
    }
    
    @Bean
    public LimitChecker limitChecker() throws LoadingFailedException {
        switch (properties.getString("limitChecker")) {
            case QUOTAS_CHECK:
                return new QuotasCheck(poolConfig.userPool());
            default:
                throw new LoadingFailedException("Wrong queue mapper configuration.");
        }
    }
}
