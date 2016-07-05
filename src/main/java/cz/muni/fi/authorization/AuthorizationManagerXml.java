/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.authorization;

import cz.muni.fi.scheduler.elementpools.IHostPool;
import cz.muni.fi.scheduler.resources.VmElement;
import java.util.List;

/**
 *
 * @author gabi
 */
public class AuthorizationManagerXml implements IAuthorizationManager {

    private final IHostPool hostPool;

    public AuthorizationManagerXml(IHostPool hostPool) {
        this.hostPool = hostPool;
    }
    
    @Override
    public List<Integer> authorize(VmElement vm) {
        return hostPool.getHostsIds();
    }
    
}
