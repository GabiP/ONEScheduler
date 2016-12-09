package cz.muni.fi.result;

import cz.muni.fi.scheduler.core.Match;
import cz.muni.fi.scheduler.elements.VmElement;
import java.util.List;

/**
 *
 * @author Gabriela Podolnikova
 */
public interface IResultManager {
    
     List<VmElement> deployPlan(List<Match> plan);
     
     List<VmElement> migrate(List<Match> migrations);
}
