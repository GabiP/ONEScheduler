/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.scheduler.fairshare;

import cz.muni.fi.scheduler.resources.VmElement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 *
 * @author Andras Urge
 */
public class FairShareOrdererTest {
       
    private FairShareOrderer orderer;
    private AbstractPriorityCalculator calculator;
        
    @Before
    public void setUp() {
        calculator = mock(AbstractPriorityCalculator.class);
        orderer = new FairShareOrderer(calculator);
    }
    
    @Test
    public void testOrderVms_EmptyInput_ReturnsEmpty() {         
        List<VmElement> vmList = new ArrayList<>();                
        when(calculator.getUserPriorities(new HashSet<>())).thenReturn(new HashMap<>());
                       
        List<VmElement> resultList = orderer.orderVms(vmList);
        
        assertTrue("Result is not empty.", resultList.isEmpty());      
    }
    
    @Test(expected = NullPointerException.class) 
    public void testOrderVms_MissingUserPriorities_ThrowsException() {   
        List<VmElement> vmList = createVmData();
        
        Set<Integer> userIds = new HashSet<>(Arrays.asList(0, 1, 2));
        Map<Integer, Float> userPriorities = new HashMap<>();
        when(calculator.getUserPriorities(userIds)).thenReturn(userPriorities);
                       
        List<VmElement> resultList = orderer.orderVms(vmList);
    }
    
    @Test
    public void testOrderVms_ReturnsAllVms() {   
        List<VmElement> vmList = createVmData();
        
        Set<Integer> userIds = new HashSet<>(Arrays.asList(0, 1, 2));
        Map<Integer, Float> userPriorities = new HashMap<>();
        userPriorities.put(0, 100f);
        userPriorities.put(1, 100f);
        userPriorities.put(2, 100f);      
        when(calculator.getUserPriorities(userIds)).thenReturn(userPriorities);
                       
        List<VmElement> resultList = orderer.orderVms(vmList);
        
        assertTrue("More VMs returned.", vmList.containsAll(resultList));
        assertTrue("Less VMs returned.", resultList.containsAll(vmList));        
    }
    
    @Test
    public void testOrderVms_ReturnsGoodOrder() {   
        List<VmElement> vmList = createVmData();
        
        Set<Integer> userIds = new HashSet<>(Arrays.asList(0, 1, 2));
        Map<Integer, Float> userPriorities = new HashMap<>();
        userPriorities.put(0, 100f);
        userPriorities.put(1, 10f);        
        userPriorities.put(2, 50f);      
        when(calculator.getUserPriorities(userIds)).thenReturn(userPriorities);
                       
        List<VmElement> resultList = orderer.orderVms(vmList);
        
        assertEquals("First VM does not match.", resultList.get(0), vmList.get(1));
        assertEquals("Second VM does not match.", resultList.get(1), vmList.get(2));
        assertEquals("Third VM does not match.", resultList.get(2), vmList.get(0));
    }
    
    private List<VmElement> createVmData() {
        List<VmElement> vmList = new ArrayList<>();
        
        VmElement vm0 = createVm(0,0);
        VmElement vm1 = createVm(1,1);
        VmElement vm2 = createVm(2,2);
        
        vmList.add(vm0);
        vmList.add(vm1);
        vmList.add(vm2);
        
        return vmList;
    }
    
    private VmElement createVm(int id, int uId) {
        VmElement vm = new VmElement();
        vm.setVmId(id);
        vm.setUid(uId);
        return vm;
    }

}
