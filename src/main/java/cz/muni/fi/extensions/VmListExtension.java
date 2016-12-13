/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.extensions;

import cz.muni.fi.scheduler.elements.VmElement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 *
 * @author Andras Urge
 */
public class VmListExtension {
    
    public static List<Integer> getVmIds(List<VmElement> vms) {
        return vms.stream().map(VmElement::getVmId).collect(Collectors.toList());
    }
    
    /**
     * Returns the set of user IDs of the inputted virtual machines.
     * 
     * @param vms to get the users ids
     * @return Set of user IDs
     */    
    public static Set<Integer> getUserIds(List<VmElement> vms) {
        return vms.stream().map(VmElement::getUid).collect(Collectors.toSet());
    }    
    
    public static List<Integer> getRuntimes(List<VmElement> vms) {
        return vms.stream().map(VmElement::getRunTime).collect(Collectors.toList());
    }
    
    public static Map<Integer, List<VmElement>> getUserVms(List<VmElement> vms) {
        Map<Integer, List<VmElement>> userVms = new HashMap<>();
        for (VmElement vm : vms) {
            if (!userVms.containsKey(vm.getUid())) {
                userVms.put(vm.getUid(), new ArrayList<>());
            } 
            userVms.get(vm.getUid()).add(vm);
        }
        return userVms;
    }
}
