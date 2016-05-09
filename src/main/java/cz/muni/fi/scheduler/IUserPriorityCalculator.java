/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.scheduler;

import java.util.Map;
import java.util.Set;

/**
 *
 * @author Andras Urge
 */
public interface IUserPriorityCalculator {
    
    public Map<Integer, Float> getUserPriorities(Set<Integer> userIds);
    
}
