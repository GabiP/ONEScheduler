/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.muni.fi.scheduler.filters;

import cz.muni.fi.scheduler.filters.hosts.strategies.FilterHostsBySchedulingRequirements;
import cz.muni.fi.scheduler.resources.HostElement;
import cz.muni.fi.scheduler.resources.VmElement;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Gabriela Podolnikova
 */
public class FilterHostsBySchedulingRequirementsTest {
    
    private VmElement vm;
    private HostElement host;
    
    private FilterHostsBySchedulingRequirements filter;
    
    @Before
    public void setUp() {
        vm = new VmElement();
        host = new HostElement();
        filter = new FilterHostsBySchedulingRequirements(); 
    }           
            
    @Test
    public void testMoreHostsInVmTemplate() {
        host.setId(4);
        vm.setSchedRequirements("ID=\"4\" | ID=\"5\" | ID=\"6\" | CLUSTER_ID=\"100\"");
        assertTrue(filter.test(vm, host));
    }
    
    @Test
    public void testNotTheHostInVmTemplate() {
        host.setId(10);
        vm.setSchedRequirements("ID=\"4\" | ID=\"5\" | ID=\"6\" | CLUSTER_ID=\"100\"");
        assertFalse(filter.test(vm, host));
    }
    
    @Test
    public void testClusterIdInVmTemplate() {
        host.setClusterId(100);
        vm.setSchedRequirements("ID=\"4\" | ID=\"5\" | ID=\"6\" | CLUSTER_ID=\"100\"");
        assertTrue(filter.test(vm, host));
    }
    
}
