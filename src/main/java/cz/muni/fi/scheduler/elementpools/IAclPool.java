/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.scheduler.elementpools;

import java.util.List;
import org.opennebula.client.acl.Acl;

/**
 *
 * @author Gabriela Podolnikova
 */
public interface IAclPool {
    
    List<Acl> getAcls();
}
