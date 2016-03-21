/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.pools;

import java.util.ArrayList;
import java.util.Iterator;
import org.opennebula.client.acl.Acl;
import org.opennebula.client.acl.AclPool;

/**
 *
 * @author Gabriela Podolnikova
 */
public class AclXml {
    
    private AclPool aclpool;
    
    private ArrayList<Acl> acls;
    
    public AclXml(AclPool aclpool) {
        this.aclpool = aclpool;
        aclpool.info();
        Iterator<Acl> itr = aclpool.iterator();
        while (itr.hasNext()) {
            Acl acl = itr.next();
            acls.add(acl);
        }   
    }

    /**
     * @return the acls
     */
    public ArrayList<Acl> getAcls() {
        return acls;
    }

    /**
     * @param acls the acls to set
     */
    public void setAcls(ArrayList<Acl> acls) {
        this.acls = acls;
    }
}
