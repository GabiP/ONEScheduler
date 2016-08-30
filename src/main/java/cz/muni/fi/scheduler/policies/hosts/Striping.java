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

/**
 * Striping policy
 * Target: Maximize the resources available to VMs in a node
 * Heuristic: Spread the VMs in the cluster nodes
 * Implementation: Use those nodes with less VMs running first
 * 
 * @author Gabriela Podolnikova
 */
public class Striping implements IPlacementPolicy {

    @Override
    public List<HostElement> sortHosts(List<HostElement> hosts, VmElement vm, SchedulerData schedulerData) {
        List<HostElement> result = new ArrayList<>();
        HostElement lessVms = null;
        Map<HostElement, Integer> runningVms = schedulerData.getRunningVms();
        int minValueInMap=(Collections.min(runningVms.values()));  // This will return max value in the Hashmap
        for (Map.Entry<HostElement, Integer> entry : runningVms.entrySet()) {  // Iterate through hashmap
            if (entry.getValue() == minValueInMap) {
                lessVms = entry.getKey();
            }
        }
        return result;
    }
}
