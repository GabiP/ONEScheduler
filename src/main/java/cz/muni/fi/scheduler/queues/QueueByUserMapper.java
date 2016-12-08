package cz.muni.fi.scheduler.queues;

import cz.muni.fi.scheduler.elementpools.IUserPool;
import cz.muni.fi.scheduler.elements.UserElement;
import cz.muni.fi.scheduler.elements.VmElement;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class maps users to queues without the fairshare calculation.
 * Each user has the same priority.
 * 
 * @author Gabriela Podolnikova
 */
public class QueueByUserMapper implements QueueMapper {
    
    private IUserPool userPool;
    
    protected final Logger log = LoggerFactory.getLogger(getClass());
    
    public QueueByUserMapper(IUserPool userPool) {
        this.userPool = userPool;
    }

    @Override
    public List<Queue> mapQueues(List<VmElement> vms) {
        List<UserElement> users = getUsers(vms);
        List<Queue> result = createQueueForUsers(users);
        for (VmElement vm: vms)  {
            Queue q = getUsersQueue(result, vm.getUid());
            q.queue(vm);
        }
        printQueues(result);
        return result;
    }
    
    private List<UserElement> getUsers(List<VmElement> vms) {
        List<UserElement> users = new ArrayList<>();
        for (VmElement vm: vms) {
            Integer userId = vm.getUid();
            UserElement user = userPool.getUser(userId);
            if (!users.contains(user)) {
                users.add(user);
            }
        }
        return users;
    }
    
    private List<Queue> createQueueForUsers(List<UserElement> users) {
        List<Queue> queues = new ArrayList<>();
        for (UserElement user: users) {
            Float userPriority = 1.00f;
            Queue q = new Queue("User" + user.getId(), userPriority, new ArrayList<>());
            log.info("A new queue was created: " + q);
            queues.add(q);
        }
        return queues;
    }

    private Queue getUsersQueue(List<Queue> queues, Integer id) {
        String queueName = "User" + id;
        for (Queue q: queues) {
            if (queueName.equals(q.getName())) {
                return q;
            }
        }
        return null;
    }
    
     private void printQueues(List<Queue> output) {
        for (Queue q: output) {
            log.info("Created queue:" + q);
        }
    }
    
}
