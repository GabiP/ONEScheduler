package cz.muni.fi.config;

import cz.muni.fi.exceptions.LoadingFailedException;
import cz.muni.fi.scheduler.limits.ILimitChecker;
import cz.muni.fi.scheduler.limits.NoLimits;
import cz.muni.fi.scheduler.limits.QuotasCheck;
import cz.muni.fi.scheduler.queues.*;
import cz.muni.fi.scheduler.queues.IQueueMapper;
import cz.muni.fi.scheduler.selectors.QueueByQueue;
import cz.muni.fi.scheduler.selectors.RoundRobin;
import cz.muni.fi.scheduler.selectors.IVmSelector;
import cz.muni.fi.scheduler.setup.FairshareConfiguration;
import cz.muni.fi.scheduler.setup.PropertiesConfig;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Configures scheduler's behavior.
 * 
 * @author Gabriela Podolnikova
 */
@Configuration
@Import({PoolConfig.class, FairshareConfig.class})
public class SchedulingConfig {
    
    @Autowired PoolConfig poolConfig;
    @Autowired FairshareConfig fairshareConfig;
    
    private PropertiesConfig properties;
    private FairshareConfiguration fairshareProperties;
    
    private static final String FIXED_NUM_OF_QUEUES = "FixedNumOfQueues";
    private static final String QUEUE_BY_USER = "QueueByUser";
    private static final String USER_FAIRSHARE = "UserFairshare";
    private static final String USER_GROUP_FAIRSHARE = "UserGroupFairshare";
    private static final String ONE_QUEUE = "OneQueue";
    
    private static final String QUEUE_BY_QUEUE = "QueueByQueue";   
    private static final String ROUND_ROBIN = "RoundRobin";
    
    private static final String QUOTAS_CHECK = "QuotasCheck";
    private static final String NO_LIMITS = "NoLimits";
    
    public SchedulingConfig() throws IOException, LoadingFailedException {
        properties = new PropertiesConfig("configuration.properties");
        fairshareProperties = new FairshareConfiguration("fairshare.properties");
    }
    
    /**
     * Creates an instance of the queue mapper policy given in configuration file.
     * @return the queue mapper.
     * @throws LoadingFailedException 
     */
    @Bean
    public IQueueMapper queueMapper() throws LoadingFailedException {
        switch (properties.getString("queueMapper")) {
            case FIXED_NUM_OF_QUEUES:
                return new FixedNumOfQueuesMapper(properties.getInt("numberofqueues"));
            case QUEUE_BY_USER:
                return new QueueByUserMapper(poolConfig.userPool());
            case USER_FAIRSHARE:
                return new UserFairshareMapper(
                        fairshareConfig.userPriorityCalculator(), 
                        fairshareProperties.getUserPercentages());
            case USER_GROUP_FAIRSHARE:
                return new UserGroupFairshareMapper(
                        fairshareConfig.userPriorityCalculator(), 
                        poolConfig.userPool(), 
                        fairshareProperties.getUserPercentages(), 
                        fairshareProperties.getUserGroupPercentages());
            case ONE_QUEUE:
                return new OneQueueMapper();
            default:
                throw new LoadingFailedException("Wrong queue mapper configuration.");
        }
    }
    
    /**
     * Creates an instance of the vm selection policy given in configuration file.
     * @return the insance of vm selector.
     * @throws LoadingFailedException 
     */
    @Bean
    public IVmSelector vmSelector() throws LoadingFailedException {
        switch (properties.getString("vmSelector")) {
            case QUEUE_BY_QUEUE:
                return new QueueByQueue();
            case ROUND_ROBIN:
                return new RoundRobin();
            default:
                throw new LoadingFailedException("Wrong queue mapper configuration.");
        }
    }
    
    /**
     * Creates an instance of the limit checking policy given in configuration file.
     * @return the limit checker configured.
     * @throws LoadingFailedException 
     */
    @Bean
    public ILimitChecker limitChecker() throws LoadingFailedException {
        switch (properties.getString("limitChecker")) {
            case QUOTAS_CHECK:
                return new QuotasCheck(poolConfig.userPool());
            case NO_LIMITS:
                return new NoLimits();
            default:
                throw new LoadingFailedException("Wrong queue mapper configuration.");
        }
    }
}
