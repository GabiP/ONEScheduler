/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.scheduler.authorization;

import cz.muni.fi.scheduler.elementpools.IDatastorePool;
import cz.muni.fi.scheduler.elementpools.IHostPool;
import cz.muni.fi.scheduler.elements.DatastoreElement;
import cz.muni.fi.scheduler.elements.HostElement;
import cz.muni.fi.scheduler.elements.VmElement;
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
    
    private List<HostElement> hosts = new ArrayList<>();
    private List<DatastoreElement> datastores = new ArrayList<>();

    public AuthorizationManagerXml(IHostPool hostPool, IDatastorePool dsPool) {
        hosts = hostPool.getActiveHosts();
        datastores = dsPool.getSystemDs();
    }
    
    @Override
    public void authorize(VmElement vm) {
    }

    @Override
    public List<HostElement> getAuthorizedHosts() {
        return hosts;
    }

    @Override
    public List<DatastoreElement> getAuthorizedDs() {
        return datastores;
    }       
}
