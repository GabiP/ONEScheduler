package cz.muni.fi.scheduler.policies.hosts;

import cz.muni.fi.scheduler.core.SchedulerData;
import cz.muni.fi.scheduler.elements.HostElement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Load-aware policy:
 * Target: Maximize the resources available to VMs in a node
 * Heuristic: Use those nodes with less load
 * Implementation: Use those nodes with more FREE_CPU first
 * 
 * @author Gabriela Podolnikova
 */
public class LoadAware implements IPlacementPolicy {

    protected final Logger log = LoggerFactory.getLogger(getClass());
    
    @Override
    public List<HostElement> sortHosts(List<HostElement> hosts, SchedulerData schedulerData) {
        List<HostElement> result = new ArrayList<>();
        Map<HostElement, Float> freeCpus = getFreeCpus(hosts, schedulerData);
        /*Float maxValueInMap=(Collections.max(freeCpus.values()));  // This will return max value in the Hashmap
        for (Map.Entry<HostElement, Float> entry : freeCpus.entrySet()) {  // Iterate through hashmap
            if (entry.getValue() == maxValueInMap) {
                HostElement maxValue = entry.getKey();
            }
        }*/
        log.info("List of hosts: " + hosts);
        result.addAll(sortByValue(freeCpus).keySet());
        log.info("List of sorted hosts: " + result);
        return result;
    }
    
    private Map<HostElement, Float> getFreeCpus(List<HostElement> hosts, SchedulerData schedulerData) {
        Map<HostElement, Float> freeCpus = new HashMap<>();
        for (HostElement host: hosts) {
            Float maxCpu = host.getMax_cpu();
            Float cpuUsage = schedulerData.getReservedCpu(host) + host.getCpu_usage();
            Float freeCpu = maxCpu - cpuUsage;
            freeCpus.put(host, freeCpu);
        }
        return freeCpus;
    }
    
    private static <K, V extends Comparable<? super V>>  Map<K, V> sortByValue(Map<K, V> map) {
        return map.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Collections.reverseOrder()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }
}
