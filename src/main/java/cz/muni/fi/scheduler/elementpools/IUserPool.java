/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.scheduler.elementpools;

import cz.muni.fi.scheduler.resources.UserXml;
import java.util.List;

/**
 *
 * @author Gabriela Podolnikova
 */
public interface IUserPool {
    
    public List<UserXml> getUsers();
    
    public UserXml getById(int id);
}
