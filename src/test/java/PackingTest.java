
import cz.muni.fi.scheduler.SchedulerData;
import cz.muni.fi.scheduler.policies.hosts.IPlacementPolicy;
import cz.muni.fi.scheduler.policies.hosts.Packing;
import cz.muni.fi.scheduler.resources.HostElement;
import cz.muni.fi.scheduler.resources.VmElement;
import java.util.List;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Gabriela Podolnikova
 */
@RunWith(MockitoJUnitRunner.class)
public class PackingTest {
    
    @Mock
    private List<HostElement> hosts;
    
    @Mock
    private VmElement vm;
    
    @Mock
    private SchedulerData schedulerData;
        
    private Packing panckingPolicy;
    
    @Before
    public void init() {
        panckingPolicy = new Packing();
    }
    
}
