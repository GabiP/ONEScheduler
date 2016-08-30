/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.scheduler.policies.hosts;

import cz.muni.fi.scheduler.Scheduler;
import cz.muni.fi.scheduler.SchedulerData;
import cz.muni.fi.scheduler.resources.HostElement;
import cz.muni.fi.scheduler.resources.VmElement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
        HostElement moreVms = null;
        Map<HostElement, Integer> runningVms = schedulerData.getRunningVms();
        int maxValueInMap=(Collections.max(runningVms.values()));  // This will return max value in the Hashmap
        for (Entry<HostElement, Integer> entry : runningVms.entrySet()) {  // Iterate through hashmap
            if (entry.getValue() == maxValueInMap) {
                moreVms = entry.getKey();
            }
        }
        return result;
    }
    
}
