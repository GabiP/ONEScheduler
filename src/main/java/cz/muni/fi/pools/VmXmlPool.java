/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.pools;

import cz.muni.fi.resources.VmXml;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import org.opennebula.client.Client;
import org.opennebula.client.OneResponse;
import org.opennebula.client.vm.VirtualMachine;
import org.opennebula.client.vm.VirtualMachinePool;

/**
 *
 * @author Gabriela Podolnikova
 */
public class VmXmlPool {
    
    private VirtualMachinePool vmp;
    
    private ArrayList<VmXml> pendingVms;
    
    public ArrayList<VmXml> loadVms(Client oneClient) throws IOException {
        setPendingVms(new ArrayList<>());
        setVmp(new VirtualMachinePool(oneClient));
        OneResponse vmpr = getVmp().info();
        if (vmpr.isError()) {
            //TODO: log it
            System.out.println(vmpr.getErrorMessage());
        }
        Iterator<VirtualMachine> itr = getVmp().iterator();
        while (itr.hasNext()) {
            VirtualMachine element = itr.next();
            System.out.println("VirtualMachine: " + element + "   state: " + element.state() + "   lcm_state: " + element.lcmState() + " ");
            //getting pendings
            if (element.state() == 1 || element.state() == 2) {
                VmXml vm = new VmXml(element);
                System.out.println("Vm: " + vm);
                getPendingVms().add(vm);
            }
        }
        return getPendingVms();
    }

    /**
     * @return the vmp
     */
    public VirtualMachinePool getVmp() {
        return vmp;
    }

    /**
     * @param vmp the vmp to set
     */
    public void setVmp(VirtualMachinePool vmp) {
        this.vmp = vmp;
    }

    /**
     * @return the pendingVms
     */
    public ArrayList<VmXml> getPendingVms() {
        return pendingVms;
    }

    /**
     * @param pendingVms the pendingVms to set
     */
    public void setPendingVms(ArrayList<VmXml> pendingVms) {
        this.pendingVms = pendingVms;
    }
}
