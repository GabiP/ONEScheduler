/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.authorization;

import cz.muni.fi.scheduler.resources.DatastoreElement;
import cz.muni.fi.scheduler.resources.HostElement;
import cz.muni.fi.scheduler.resources.VmElement;
import java.util.List;

/**
 * This interface represents an authorization manager.
 * 
 * @author Gabriela Podolnikova
 */
public interface IAuthorizationManager {
    public void authorize(VmElement vm);
    public List<HostElement> getAuthorizedHosts();
    public List<DatastoreElement> getAuthorizedDs();
}
