/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.scheduler.policies.hosts;

import cz.muni.fi.scheduler.SchedulerData;
import cz.muni.fi.scheduler.resources.HostElement;
import cz.muni.fi.scheduler.resources.VmElement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Packing policy:
 * Target: Minimize the number of cluster nodes in use
 * Heuristic: Pack the VMs in the cluster nodes to reduce VM fragmentation
 * Implementation: Use those nodes with more VMs running first
 * 
 * @author Gabriela Podolnikova
 */
public class Packing implements IPlacementPolicy {

    @Override
    public List<HostElement> sortHosts(List<HostElement> hosts, VmElement vm, SchedulerData schedulerData) {
        List<HostElement> result = new ArrayList<>();
        /*HostElement moreVms = null;
        Map<HostElement, Integer> runningVms = schedulerData.getRunningVmsReservation();
        int maxValueInMap=(Collections.max(runningVms.values()));  // This will return max value in the Hashmap
        for (Entry<HostElement, Integer> entry : runningVms.entrySet()) {  // Iterate through hashmap
            if (entry.getValue() == maxValueInMap) {
                moreVms = entry.getKey();
            }
        }*/
        Map<HostElement, Integer> listOfRunningVms = schedulerData.getActualRunningVms(hosts);
        result.addAll(sortByValue(listOfRunningVms).keySet());
        return result;
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
