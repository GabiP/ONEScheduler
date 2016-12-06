/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.muni.fi.scheduler.filters.datastores.strategies;

import cz.muni.fi.scheduler.filters.hosts.strategies.FilterHostsBySchedulingRequirements;
import cz.muni.fi.scheduler.resources.DatastoreElement;
import cz.muni.fi.scheduler.resources.HostElement;
import cz.muni.fi.scheduler.resources.VmElement;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 *
 * @author Gabriela Podolnikova
 */
public class FilterDatastoresBySchedulingRequirementsTest {

    private VmElement vm;
    private DatastoreElement ds;
    private HostElement host;
    
    private FilterDatastoresBySchedulingRequirements filter;
    
    @Before
    public void setUp() {
        vm = new VmElement();
        ds = new DatastoreElement();
        host = new HostElement();
        filter = new FilterDatastoresBySchedulingRequirements(); 
    }
    
    @Test
    public void testEmptyRequirements() {
        vm.setSchedRequirements("");
        assertTrue(filter.test(vm, ds, host));
    }
    
    @Test
    public void testNullRequirements() {
        assertTrue(filter.test(vm, ds, host));
    }
        
            
    @Test
    public void testDsIsSuitable() {
        ds.setId(0);
        vm.setSchedDsRequirements("ID=\"0\"");
        assertTrue(filter.test(vm, ds, host));
    }
    
    @Test
    public void testDsIsNotSuitable() {
        ds.setId(3);
        vm.setSchedDsRequirements("ID=\"0\"");
        assertFalse(filter.test(vm, ds, host));
    }
    
    @Test
    public void testDsIsSuitableWithMoreIds() {
        ds.setId(3);
        vm.setSchedDsRequirements("ID=\"0\" | ID=\"3\"");
        assertTrue(filter.test(vm, ds, host));
    }
    
    @Test
    public void testDsIsNotSuitableWithMoreIds() {
        ds.setId(4);
        vm.setSchedDsRequirements("ID=\"0\" | ID=\"3\"");
        assertFalse(filter.test(vm, ds, host));
    }
    
    @Test
    public void testDsIsSuitableWithName() {
        ds.setName("system");
        vm.setSchedDsRequirements("NAME=\"system\" | ID=\"3\"");
        assertTrue(filter.test(vm, ds, host));
    }
    
    @Test
    public void testDsIsNotSuitableWithName() {
        ds.setName("image");
        vm.setSchedDsRequirements("NAME=\"system\" | ID=\"3\"");
        assertFalse(filter.test(vm, ds, host));
    }
}
