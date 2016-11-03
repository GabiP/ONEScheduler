package cz.muni.fi.result;

import cz.muni.fi.scheduler.resources.HostElement;
import java.util.List;

/**
 * This class serves for deploying the results from scheduling.
 * 
 * @author Gabriela Podolnikova
 */
public class OneResultManager implements IResultManager {

    @Override
    public boolean writeResults(List<HostElement> hostPool) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    /*public List<VmElement> deployPlan(Map<HostElement, List<VmElement>> plan) {
        List<VmElement> failedVms = new ArrayList<>();
        Set<HostElement> hosts = plan.keySet();
        for (HostElement host: hosts) {
            List<VmElement> vms = plan.get(host);
            for (VmElement vm: vms) {
                OneResponse oneResp = .deploy(host.getId());
                if (oneResp.getMessage() == null) {
                    //Log error in deployment
                    System.out.println(oneResp.getErrorMessage());
                    failedVms.add(vm);
                }
            }
        }
        return failedVms;
    }*/
}
