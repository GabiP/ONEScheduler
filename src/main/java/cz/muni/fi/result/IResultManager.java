package cz.muni.fi.result;

import cz.muni.fi.scheduler.core.Match;
import cz.muni.fi.scheduler.elements.VmElement;
import java.util.List;

/**
 * This interface is provided for handling the VM deployment process.
 * 
 * @author Gabriela Podolnikova
 */
public interface IResultManager {
    
    /**
     * Takes the given plan. And each VM is deployed on the given resource.
     * If the deployment fails, the VM will be returned as failed. Thus, VM is not deployed.
     * 
     * @param plan the plan with the matched VMs
     * @return the failed VMs
     */
     List<VmElement> deployPlan(List<Match> plan);
     
     /**
      * Takes the given plan. And each VM is migrated on the given resource.
      * If the migration fails, the VM will be returned as failed. Thus, VM is not migrated.
      * 
      * @param migrations the plan with the matched VMs
      * @return the failed VMs
      */
     List<VmElement> migrate(List<Match> migrations);
}
