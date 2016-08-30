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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Load-aware policy:
 * Target: Maximize the resources available to VMs in a node
 * Heuristic: Use those nodes with less load
 * Implementation: Use those nodes with more FREE_CPU first
 * 
 * @author Gabriela Podolnikova
 */
public class LoadAware implements IPlacementPolicy {

    @Override
    public List<HostElement> sortHosts(List<HostElement> hosts, VmElement vm, SchedulerData schedulerData) {
        List<HostElement> result = new ArrayList<>();
        HostElement maxValue = null;
        Map<HostElement, Float> cpuUsages = schedulerData.getCpuUsages();
        Map<HostElement, Float> freeCpus = getFreeCpus(hosts, cpuUsages);
        Float maxValueInMap=(Collections.max(freeCpus.values()));  // This will return max value in the Hashmap
        for (Map.Entry<HostElement, Float> entry : freeCpus.entrySet()) {  // Iterate through hashmap
            if (entry.getValue() == maxValueInMap) {
                maxValue = entry.getKey();
            }
        }
        return result;
    }
    
    private Map<HostElement, Float> getFreeCpus(List<HostElement> hosts, Map<HostElement, Float> cpuUsages) {
        Map<HostElement, Float> freeCpus = new HashMap<>();
        for (HostElement host: hosts) {
            Float maxCpu = host.getMax_cpu();
            Float cpuUsage = cpuUsages.get(host);
            Float freeCpu = maxCpu - cpuUsage;
            freeCpus.put(host, freeCpu);
        }
        return freeCpus;
    }
    
}
