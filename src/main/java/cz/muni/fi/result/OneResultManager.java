package cz.muni.fi.result;

import cz.muni.fi.scheduler.core.Match;
import cz.muni.fi.scheduler.resources.VmElement;
import java.util.ArrayList;
import java.util.List;
import org.opennebula.client.OneResponse;
import org.opennebula.client.vm.VirtualMachine;
import org.opennebula.client.vm.VirtualMachinePool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class serves for deploying the results from scheduling.
 * 
 * @author Gabriela Podolnikova
 */
public class OneResultManager implements IResultManager {
    
    protected final Logger log = LoggerFactory.getLogger(getClass());
       
    private VirtualMachinePool oneVmPool;

    public OneResultManager(VirtualMachinePool oneVmPool) {
        this.oneVmPool = oneVmPool;
    }
    
    @Override
    public List<VmElement> deployPlan(List<Match> plan) {
        List<VmElement> failedVms = new ArrayList<>();
        for (Match match: plan) {
            Integer hostId = match.getHost().getId();
            Integer dsId = match.getDatastore().getId();
            List<VmElement> vms = match.getVms();
            for (VmElement vm: vms) {
                VirtualMachine virtualMachine = oneVmPool.getById(vm.getVmId());
                OneResponse oneResp = virtualMachine.deploy(hostId, true, dsId);
                if (oneResp.getMessage() == null) {
                    log.error("Deployment failed of VM: " + virtualMachine.getId() + " oneResponse: " + oneResp.getErrorMessage());
                    failedVms.add(vm);
                }
            }
        }
        return failedVms;
    }

    @Override
    public List<VmElement> migrate(List<Match> migrations) {
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
