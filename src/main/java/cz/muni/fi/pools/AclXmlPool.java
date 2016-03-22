/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.pools;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.opennebula.client.Client;
import org.opennebula.client.OneResponse;
import org.opennebula.client.acl.Acl;
import org.opennebula.client.acl.AclPool;
import org.opennebula.client.acl.RuleParseException;
import cz.muni.fi.scheduler.Scheduler;

/**
 *
 * @author Gabriela Podolnikova
 */
public class AclXmlPool {
    
    private AclPool aclpool;
    
    private ArrayList<Acl> acls;
    
    public ArrayList<Acl> loadAcl(Client oneClient) {
        acls = new ArrayList<>();
        aclpool = new AclPool(oneClient);
        OneResponse aclpr = aclpool.info();
        if (aclpr.isError()) {
            //TODO: log it
            System.out.println(aclpr.getErrorMessage());
        }
        Iterator<Acl> itr = aclpool.iterator();
        while (itr.hasNext()) {
            Acl el = itr.next();
            System.out.println("Acl rule number: " + el.getId() + " resources: " + el.resource() + " rights: " + el.rights() + " users: " + el.user() + " toString: " + el.toString());
            String[] parsedRule = null;
            try {
                parsedRule = Acl.parseRule(el.toString());
            } catch (RuleParseException ex) {
                Logger.getLogger(Scheduler.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println(parsedRule[0]);
            System.out.println(parsedRule[1]);
            System.out.println(parsedRule[2]);
            acls.add(el);
        }
        return acls;
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