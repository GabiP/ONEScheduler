/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.scheduler.elementpools;

import cz.muni.fi.scheduler.elements.HostElement;
import java.util.List;

/**
 *
 * @author Gabriela Podolnikova
 */
public interface IHostPool {
    
    List<HostElement> getHosts();
    
    List<HostElement> getActiveHosts();
    
    List<Integer> getHostsIds();
    
    HostElement getHost(int id);
}
