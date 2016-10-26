/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.extensions;

import cz.muni.fi.scheduler.resources.VmElement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Andras Urge
 */
public class VmListExtension {
    
    public static List<Integer> getVmIds(List<VmElement> vms) {
        List<Integer> vmIds = new ArrayList<>();
        for (VmElement vm : vms) {
            vmIds.add(vm.getVmId());
        }
        return vmIds;
    }
    
    /**
     * Returns the set of user IDs of the inputted virtual machines.
     * 
     * @param vms
     * @return Set of user IDs
     */    
    public static Set<Integer> getUserIds(List<VmElement> vms) {
        Set<Integer> userIds = new HashSet<>();
        for (VmElement vm : vms) {
            userIds.add(vm.getUid());
        }
        return userIds;
    }    
    
    public static List<Integer> getRuntimes(List<VmElement> vms) {
        List<Integer> vmRuntimes = new ArrayList<>();
        for (VmElement vm : vms) {
            vmRuntimes.add(vm.getRunTime());
        }
        return vmRuntimes;
    }
}
