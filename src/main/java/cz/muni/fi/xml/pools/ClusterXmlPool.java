/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.xml.pools;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import cz.muni.fi.scheduler.elementpools.IClusterPool;
import cz.muni.fi.scheduler.resources.ClusterElement;
import cz.muni.fi.xml.mappers.ClusterXmlMapper;
import cz.muni.fi.xml.resources.ClusterJacksonPool;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Gabriela Podolnikova
 */
public class ClusterXmlPool implements IClusterPool {

    List<ClusterElement> clusters;

    public ClusterXmlPool(String xml) throws IOException {
        XmlMapper xmlMapper = new XmlMapper();
        ClusterJacksonPool clPool = xmlMapper.readValue(xml, ClusterJacksonPool.class);
        clusters = ClusterXmlMapper.map(clPool.getClusters());
    }
    
    @Override
    public List<ClusterElement> getClusters() {
        return Collections.unmodifiableList(clusters);
    }

    @Override
    public ClusterElement getCluster(int id) {
        for (ClusterElement cl : clusters) {
            if (cl.getId() == id) {
                return cl;
            }
        }
        return null;
    }
    
}
