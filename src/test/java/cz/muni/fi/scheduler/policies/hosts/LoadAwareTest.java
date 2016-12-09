/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.muni.fi.scheduler.policies.hosts;

import cz.muni.fi.scheduler.core.SchedulerData;
import cz.muni.fi.scheduler.elements.HostElement;
import java.util.ArrayList;
import java.util.List;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.runners.MockitoJUnitRunner;

/**
 *
 * @author Gabriela Podolnikova
 */
@RunWith(MockitoJUnitRunner.class)
public class LoadAwareTest {
    
    private List<HostElement> hosts;

    HostElement host1;
    HostElement host2;
    HostElement host3;
    HostElement host4;
    HostElement host5;
    HostElement host6;

    @Mock
    private SchedulerData schedulerData;

    private LoadAware policy;

    @Before
    public void init() {
        hosts = new ArrayList<>();
        host1 = new HostElement();
        host1.setId(1);
        host1.setMax_cpu(2.0f);
        host1.setCpu_usage(0.5f);

        host2 = new HostElement();
        host2.setId(2);
        host2.setMax_cpu(1.0f);
        host2.setCpu_usage(0.0f);

        host3 = new HostElement();
        host3.setId(3);
        host3.setMax_cpu(3.0f);
        host3.setCpu_usage(0.5f);

        host4 = new HostElement();
        host4.setId(4);
        host4.setMax_cpu(1.0f);
        host4.setCpu_usage(0.0f);

        host5 = new HostElement();
        host5.setId(5);
        host5.setMax_cpu(2.0f);
        host5.setCpu_usage(0.1f);
        
        host6 = new HostElement();
        host6.setId(6);
        host6.setMax_cpu(1.0f);
        host6.setCpu_usage(0.5f);

        hosts.add(host1);
        hosts.add(host2);
        hosts.add(host3);
        hosts.add(host4);
        hosts.add(host5);
        hosts.add(host6);
        policy = new LoadAware();
    }

    @Test
    public void sortByValue() {
        when(schedulerData.getReservedCpu(host1)).thenReturn(0.5f);
        when(schedulerData.getReservedCpu(host4)).thenReturn(0.3f);
        List<HostElement> result = policy.sortHosts(hosts, schedulerData);

        List<HostElement> expectedResult = new ArrayList<>();
        expectedResult.add(host3);
        expectedResult.add(host5);
        expectedResult.add(host1);
        expectedResult.add(host2);
        expectedResult.add(host4);
        expectedResult.add(host6);

        assertThat(result, is(expectedResult));
    }
}
