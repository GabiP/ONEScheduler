/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.one.pools;

import java.util.ArrayList;
import java.util.Iterator;
import org.opennebula.client.Client;
import org.opennebula.client.OneResponse;
import org.opennebula.client.acl.Acl;
import org.opennebula.client.acl.AclPool;
import cz.muni.fi.scheduler.elementpools.IAclPool;
import java.util.List;

/**
 *
 * @author Gabriela Podolnikova
 */
public class AclElementPool implements IAclPool{

    private final AclPool aclpool;

    public AclElementPool(Client oneClient) {
        aclpool = new AclPool(oneClient);
    }

    @Override
    public List<Acl> getAcls() {
        List<Acl> acls = new ArrayList<>();
        OneResponse aclpr = aclpool.info();
        if (aclpr.isError()) {
            //TODO: log it
            System.out.println(aclpr.getErrorMessage());
        }
        Iterator<Acl> itr = aclpool.iterator();
        while (itr.hasNext()) {
            Acl el = itr.next();
            System.out.println("Acl rule number: " + el.getId() + " toString: " + el.toString());
            acls.add(el);
        }
        return acls;
    }

}
