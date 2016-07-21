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
 * This class represents authorization manager for XML representation of cloud resources.
 * We do not provide ACLs XML file. Therefore we assume that all users can use all resources.
 * Which means that for every VM we return all active hosts available in the system.
 * 
 * @author Gabriela Podolnikova
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