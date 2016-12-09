/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.scheduler.fairshare;

import cz.muni.fi.scheduler.elementpools.IVmPool;
import cz.muni.fi.scheduler.fairshare.historyrecords.IUserFairshareRecordManager;
import cz.muni.fi.scheduler.fairshare.historyrecords.IVmFairshareRecordManager;
import cz.muni.fi.scheduler.fairshare.historyrecords.VmFairshareRecord;
import cz.muni.fi.scheduler.elements.VmElement;
import cz.muni.fi.scheduler.elements.nodes.HistoryNode;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Ignore;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

/**
 *
 * @author Andras Urge
 */
@Ignore
public class AbstractPriorityCalculatorTest {
        
    private UserPriorityCalculator calculator;
    private IVmPool vmPool;     
    private IUserFairshareRecordManager userRecordManager;
    private IVmFairshareRecordManager vmRecordManager;
        
    @Before
    public void setUp() {
        vmPool = mock(IVmPool.class);
        userRecordManager = mock(IUserFairshareRecordManager.class);
        vmRecordManager = mock(IVmFairshareRecordManager.class);
        /*calculator = spy(new UserPriorityCalculator(vmPool, userRecordManager, vmRecordManager) {
            @Override
            protected float getPenalty(VmElement vm) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        });*/
    }
    @Test
    public void testGetUserPriorities_EmptyInput_ReturnsEmpty() {        
        Set<Integer> userIds = new HashSet<>();
        Map<Integer, Float> result = calculator.getUserPriorities(userIds);
        
        assertTrue("Result is not empty.", result.isEmpty());
    }
    
    @Test
    public void testGetUserPriorities_WithFinishedVms() {
        List<VmElement> finishedVms = createFinishedVms();
        
        when(vmPool.getVms(1, 6)).thenReturn(finishedVms);
        when(vmRecordManager.createVmFromRecord(eq(finishedVms.get(0)), any(VmFairshareRecord.class))).thenReturn(finishedVms.get(0));
        when(vmRecordManager.createVmFromRecord(eq(finishedVms.get(1)), any(VmFairshareRecord.class))).thenReturn(finishedVms.get(1));
        //doReturn(10f).when(calculator).getPenalty(finishedVms.get(0));
        //doReturn(20f).when(calculator).getPenalty(finishedVms.get(1));
        
        Set<Integer> userIds = new HashSet<>(Arrays.asList(1));
        Map<Integer, Float> result = calculator.getUserPriorities(userIds);
                
        double delta = 0.0001; 
        assertEquals("Priority does not match.", 70000, result.get(1), delta);
    }
    
    @Test
    public void testGetUserPriorities_WithFreshVms() {
        List<VmElement> freshVms = createFreshVms();
        
        when(vmPool.getVmsByUser(1)).thenReturn(freshVms);
        when(vmPool.getAllVmsByUser(1)).thenReturn(freshVms);
        //doReturn(10f).when(calculator).getPenalty(freshVms.get(0));
        //doReturn(20f).when(calculator).getPenalty(freshVms.get(1));
        
        Set<Integer> userIds = new HashSet<>(Arrays.asList(1));
        Map<Integer, Float> result = calculator.getUserPriorities(userIds);
                
        double delta = 0.0001; 
        assertEquals("Priority does not match.", 0, result.get(1), delta);
    }
    
    @Test
    public void testGetUserPriorities_WithActiveVms() {
        List<VmElement> activeVms = createActiveVms();  
              
        when(vmPool.getVmsByUser(1)).thenReturn(activeVms);
        when(vmRecordManager.createVmFromRecord(eq(activeVms.get(0)), any(VmFairshareRecord.class))).thenReturn(activeVms.get(0));
        when(vmRecordManager.createVmFromRecord(eq(activeVms.get(1)), any(VmFairshareRecord.class))).thenReturn(activeVms.get(1));
        //doReturn(10f).when(calculator).getPenalty(activeVms.get(0));
        //doReturn(20f).when(calculator).getPenalty(activeVms.get(1));
        
        Set<Integer> userIds = new HashSet<>(Arrays.asList(1));
        Map<Integer, Float> result = calculator.getUserPriorities(userIds);
                
        double delta = 0.0001; 
        assertEquals("Priority does not match.", 70000, result.get(1), delta);
    }
    
    @Test
    public void testGetUserPriorities_WithFreshAndFinishedVms() {
        List<VmElement> freshVms = createFreshVms();
        List<VmElement> finishedVms = createFinishedVms();
        List<VmElement> allVms = new ArrayList<>(freshVms);
        allVms.addAll(finishedVms);        
                
        when(vmPool.getVmsByUser(1)).thenReturn(freshVms);
        when(vmPool.getVms(1, 6)).thenReturn(finishedVms);
        when(vmPool.getAllVmsByUser(1)).thenReturn(allVms);
        when(vmRecordManager.createVmFromRecord(eq(finishedVms.get(0)), any(VmFairshareRecord.class))).thenReturn(finishedVms.get(0));
        when(vmRecordManager.createVmFromRecord(eq(finishedVms.get(1)), any(VmFairshareRecord.class))).thenReturn(finishedVms.get(1));
        /*doReturn(10f).when(calculator).getPenalty(finishedVms.get(0));
        doReturn(20f).when(calculator).getPenalty(finishedVms.get(1));
        doReturn(30f).when(calculator).getPenalty(freshVms.get(0));
        doReturn(40f).when(calculator).getPenalty(freshVms.get(1));*/
        
        Set<Integer> userIds = new HashSet<>(Arrays.asList(1));
        Map<Integer, Float> result = calculator.getUserPriorities(userIds);
                
        double delta = 0.0001; 
        assertEquals("Priority does not match.", 280000, result.get(1), delta);
    }
    
    @Test
    public void testGetUserPriorities_WithExistingPriorityRecords() {
        List<VmElement> finishedVms = createFinishedVms();
        VmFairshareRecord vmRecord = new VmFairshareRecord(2, 1, 25000, 0, 0.1f, 128);
        List<VmFairshareRecord>  userVmRecords = new ArrayList<>();
        userVmRecords.add(vmRecord);
                
        when(vmPool.getVms(1, 6)).thenReturn(finishedVms);        
        when(vmPool.getVm(2)).thenReturn(finishedVms.get(1));        
        when(userRecordManager.getPriority(1)).thenReturn(15000f);
        when(vmRecordManager.getRecord(2)).thenReturn(vmRecord);        
        when(vmRecordManager.getRecords(1)).thenReturn(userVmRecords);        
        when(vmRecordManager.createVmFromRecord(finishedVms.get(1), vmRecord)).thenReturn(finishedVms.get(1));
        //doReturn(20f).when(calculator).getPenalty(finishedVms.get(1));
        
        Set<Integer> userIds = new HashSet<>(Arrays.asList(1));
        Map<Integer, Float> result = calculator.getUserPriorities(userIds);
                
        double delta = 0.0001; 
        assertEquals("Priority does not match.", 80000, result.get(1), delta);
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
    
    private List<VmElement> createFreshVms() {
        List<VmElement> vmList = new ArrayList<>();
        VmElement vm1 = createVm(3, 1, 2);
        VmElement vm2 = createVm(4, 1, 2);
        vm1.setHistories(new ArrayList<>());
        vm2.setHistories(new ArrayList<>());
        vmList.add(vm1);
        vmList.add(vm2);
        
        return vmList;
    }
    
    private List<VmElement> createActiveVms() {
        List<VmElement> vmList = new ArrayList<>();
        
        VmElement vm1 = createVm(1, 1, 8);             
        HistoryNode h1 = createHistoryNode(0, 1000, 2000);
        
        List<HistoryNode> vm1Histories = new ArrayList<>();  
        vm1Histories.add(h1);    
        vm1.setHistories(vm1Histories);
        
        VmElement vm2 = createVm(2, 1, 8);     
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
        vm.setCpu(0.1f);
        vm.setMemory(128);
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
