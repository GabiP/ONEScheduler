/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.authorization;

import cz.muni.fi.scheduler.elementpools.IHostPool;
import cz.muni.fi.scheduler.resources.HostElement;
import cz.muni.fi.scheduler.resources.VmElement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author gabi
 */
public class AuthorizationManagerXml implements IAuthorizationManager {

    private final IHostPool hostPool;
    
    private List<HostElement> hosts = new ArrayList<>();

    public AuthorizationManagerXml(IHostPool hostPool) {
        this.hostPool = hostPool;
        hosts = hostPool.getActiveHosts();
    }
    
    @Override
    public List<Integer> authorize(VmElement vm) {
        List<Integer> result = new ArrayList<>();
        for (HostElement host: hosts) {
            result.add(host.getId());
        }
        return result;
        //return hostPool.getHostsIds();
    }
    
}
