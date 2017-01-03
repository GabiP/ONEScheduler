package cz.muni.fi.result;

import cz.muni.fi.scheduler.core.Match;
import cz.muni.fi.scheduler.elements.VmElement;
import java.util.ArrayList;
import java.util.List;
import org.opennebula.client.OneResponse;
import org.opennebula.client.vm.VirtualMachine;
import org.opennebula.client.vm.VirtualMachinePool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class serves for deploying the the matched VM on the resource in OpenNebula.
 * 
 * @author Gabriela Podolnikova
 */
public class OneResultManager implements IResultManager {
    
    protected final Logger log = LoggerFactory.getLogger(getClass());
       
    private VirtualMachinePool oneVmPool;

    public OneResultManager(VirtualMachinePool oneVmPool) {
        this.oneVmPool = oneVmPool;
    }
    
    /**
     * Deploys the pending VMs.
     * Deploy parameters:
     *  hostId - The host id (hid) of the target host where the VM will be instantiated.
     *  enforce - If it is set to true, the host capacity will be checked, and the deployment will fail if the host is overcommited. Defaults to false
     *  dsId - The System Datastore where to deploy the VM. To use the default, set it to -1
     * @param plan the list of matched Host-Ds-VMs
     * @return VMs that were not deployed due to some error (logged).
     */
    @Override
    public List<VmElement> deployPlan(List<Match> plan) {
        oneVmPool.infoAll();
        List<VmElement> failedVms = new ArrayList<>();
        for (Match match: plan) {
            Integer hostId = match.getHost().getId();
            Integer dsId = match.getDatastore().getId();
            List<VmElement> vms = match.getVms();
            for (VmElement vm: vms) {
                VirtualMachine virtualMachine;
                try {
                    virtualMachine = oneVmPool.getById(vm.getVmId());
                    OneResponse oneResp = virtualMachine.deploy(hostId, true, dsId);
                    if (oneResp.getMessage() == null) {
                        log.error("Deployment failed of VM: " + virtualMachine.getId() + " oneResponse: " + oneResp.getErrorMessage());
                        failedVms.add(vm);
                    }
                } catch (Exception e) {
                    log.info("The VM " + vm.getVmId() + " was not found.");
                }
            }
        }
        return failedVms;
    }

    /**
     * Migrating List of virtual machines with ersched flag.
     * Migrate parameters:
     *  hostId - The target host id (hid) where we want to migrate the vm.
     *  live - If true we are indicating that we want live migration, otherwise false.
     *  enforce - If it is set to true, the host capacity will be checked, and the deployment will fail if the host is overcommited. Defaults to false
     *  ds_id - The System Datastore where to migrate the VM. To use the current one, set it to -1
     * @param migrations the list of matched Host-Ds-VMs
     * @return VMs that were not migrations due to some error (logged).
     */
    @Override
    public List<VmElement> migrate(List<Match> migrations) {
        oneVmPool.info();
        List<VmElement> failedVms = new ArrayList<>();
        for (Match match: migrations) {
            Integer hostId = match.getHost().getId();
            Integer dsId = match.getDatastore().getId();
            List<VmElement> vms = match.getVms();
            for (VmElement vm: vms) {
                VirtualMachine virtualMachine = oneVmPool.getById(vm.getVmId());
                OneResponse oneResp = virtualMachine.migrate(hostId, true, true, dsId);
                if (oneResp.getMessage() == null) {
                    log.error("Migration failed of VM: " + virtualMachine.getId() + " oneResponse: " + oneResp.getErrorMessage());
                    failedVms.add(vm);
                }
            }
        }
        return failedVms;
    }
}
