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
import java.util.logging.Level;
import java.util.logging.Logger;
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

    private ArrayList<VmXml> vms;

    private final Client oneClient;

    public VmXmlPool(Client oneClient) {
        this.oneClient = oneClient;
    }
    
    /**
     * Loads all hosts from OpenNebula VirtualMachinePool.
     * Retrieves and store the xml representation as VmXml object into an array of vms
     * @return array of vms
     */
    public ArrayList<VmXml> loadVms() throws IOException {
        vms = new ArrayList<>();
        vmp = new VirtualMachinePool(oneClient);
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
            VmXml vm = new VmXml(element);
            System.out.println("Vm: " + vm);
            vms.add(vm);
        }
        return vms;
    }

    /**
     * Gets virtual machines that are pending.
     * Vm states:  1 = pending
     *             2 = hold
     * @return array of pending virtual machines
     */
    public ArrayList<VmXml> getPendings() {
        ArrayList<VmXml> pendings = new ArrayList<>();
        for (VmXml vm: vms) {
            if (vm.getState() == 1 || vm.getState() == 2) {
                pendings.add(vm);
            }
        }
        return pendings;
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
    public ArrayList<VmXml> getVms() {
        return vms;
    }

    /**
     * @param vms the pendingVms to set
     */
    public void setPendingVms(ArrayList<VmXml> vms) {
        this.vms = vms;
    }
}
