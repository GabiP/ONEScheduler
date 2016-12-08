/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.scheduler.authorization;

import cz.muni.fi.scheduler.elements.DatastoreElement;
import cz.muni.fi.scheduler.elements.HostElement;
import cz.muni.fi.scheduler.elements.VmElement;
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
