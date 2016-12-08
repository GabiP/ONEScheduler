/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.scheduler.elementpools;

import cz.muni.fi.scheduler.elements.ClusterElement;
import java.util.List;

/**
 *
 * @author Gabriela Podolnikova
 */
public interface IClusterPool {
    
    public List<ClusterElement> getClusters();
    
    public ClusterElement getCluster(int id);
}
