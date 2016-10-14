/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.scheduler.fairshare;

import cz.muni.fi.scheduler.elementpools.IVmPool;
import cz.muni.fi.scheduler.resources.VmElement;
import cz.muni.fi.scheduler.resources.nodes.HistoryNode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

/**
 *
 * @author Andras Urge
 */
public class AbstractPriorityCalculatorTest {
        
    private AbstractPriorityCalculator calculator;
    private IVmPool vmPool; 
    private boolean useHistoryRecords = false;
        
    @Before
    public void setUp() {
        vmPool = mock(IVmPool.class);
        calculator = spy(new AbstractPriorityCalculator(vmPool, useHistoryRecords) {
            @Override
            protected float getPenalty(VmElement vm) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        });
    }
    @Test
    public void testGetUserPriorities_EmptyInput_ReturnsEmpty() {        
        Set<Integer> userIds = new HashSet<>();
        Map<Integer, Float> result = calculator.getUserPriorities(userIds);
        
        assertTrue("Result is not empty.", result.isEmpty());
    }
    
    @Test
    public void testGetUserPriorities_WithFinishedVms() {
        List<VmElement> user1Vms = createFinishedVms();
        
        when(vmPool.getAllVmsByUser(1)).thenReturn(user1Vms);
        doReturn(10f).when(calculator).getPenalty(user1Vms.get(0));
        doReturn(20f).when(calculator).getPenalty(user1Vms.get(1));
        
        Set<Integer> userIds = new HashSet<>(Arrays.asList(1));
        Map<Integer, Float> result = calculator.getUserPriorities(userIds);
                
        double delta = 0.0001; 
        assertEquals("Priority does not match.", 70000, result.get(1), delta);
    }
    
    @Test
    public void testGetUserPriorities_WithFreshVm() {
        List<VmElement> user1Vms = createFinishedVms();
        VmElement freshVm = createVm(3, 1, 2);
        freshVm.setHistories(new ArrayList<>());
        user1Vms.add(freshVm);
        
        when(vmPool.getAllVmsByUser(1)).thenReturn(user1Vms);
        doReturn(10f).when(calculator).getPenalty(user1Vms.get(0));
        doReturn(20f).when(calculator).getPenalty(user1Vms.get(1));
        doReturn(30f).when(calculator).getPenalty(user1Vms.get(2));
        
        Set<Integer> userIds = new HashSet<>(Arrays.asList(1));
        Map<Integer, Float> result = calculator.getUserPriorities(userIds);
                
        double delta = 0.0001; 
        assertEquals("Priority does not match.", 160000, result.get(1), delta);
    }
    
    private List<VmElement> createFinishedVms() {
        List<VmElement> vmList = new ArrayList<>();
        
        VmElement vm1 = createVm(1, 1, 6);             
        HistoryNode h1 = createHistoryNode(0, 1000, 2000);
        
        List<HistoryNode> vm1Histories = new ArrayList<>();  
        vm1Histories.add(h1);    
        vm1.setHistories(vm1Histories);
        
        VmElement vm2 = createVm(2, 1, 6);     
        HistoryNode h2 = createHistoryNode(0, 1000, 2000);
        HistoryNode h3 = createHistoryNode(1, 3000, 5000);
        
        List<HistoryNode> vm2Histories = new ArrayList<>();
        vm2Histories.add(h2);
        vm2Histories.add(h3);        
        vm2.setHistories(vm2Histories);
        
        vmList.add(vm1);
        vmList.add(vm2);
        return vmList;
    }
    
    private VmElement createVm(int id, int uId, int state) {
        VmElement vm = new VmElement();
        vm.setVmId(id);
        vm.setUid(uId);
        vm.setState(state);
        return vm;
    }
    
    private HistoryNode createHistoryNode(int sequence, long startTime, long endTime) {
        HistoryNode history = new HistoryNode();
        history.setSequence(sequence);
        history.setStartTime(startTime);
        history.setEndTime(endTime);
        return history;
    }
}
