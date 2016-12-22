package cz.muni.fi.scheduler.policies.hosts;

import cz.muni.fi.scheduler.core.SchedulerData;
import cz.muni.fi.scheduler.elements.HostElement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Striping policy
 * Target: Maximize the resources available to VMs in a node
 * Heuristic: Spread the VMs in the cluster nodes
 * Implementation: Use those nodes with less VMs running first
 * 
 * @author Gabriela Podolnikova
 */
public class Striping implements IPlacementPolicy {

    protected final Logger log = LoggerFactory.getLogger(getClass());
    
    @Override
    public List<HostElement> sortHosts(List<HostElement> hosts, SchedulerData schedulerData) {
        List<HostElement> result = new ArrayList<>();
        /*HostElement lessVms = null;
        Map<HostElement, Integer> runningVms = schedulerData.getRunningVmsReservation();
        int minValueInMap=(Collections.min(runningVms.values()));  // This will return max value in the Hashmap
        for (Map.Entry<HostElement, Integer> entry : runningVms.entrySet()) {  // Iterate through hashmap
            if (entry.getValue() == minValueInMap) {
                lessVms = entry.getKey();
            }
        }*/
        log.info("List of hosts: " + hosts);
        Map<HostElement, Integer> listOfRunningVms = schedulerData.getActualRunningVms(hosts);
        result.addAll(sortByValue(listOfRunningVms).keySet());
        log.info("List of sorted hosts: " + result);
        return result;
    }

    private static <K, V extends Comparable<? super V>>  Map<K, V> sortByValue(Map<K, V> map) {
        return map.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(/*Collections.reverseOrder()*/))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }
}
