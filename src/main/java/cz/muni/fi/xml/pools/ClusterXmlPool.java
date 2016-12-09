/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.xml.pools;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import cz.muni.fi.scheduler.elementpools.IClusterPool;
import cz.muni.fi.scheduler.elements.ClusterElement;
import cz.muni.fi.xml.mappers.ClusterXmlMapper;
import cz.muni.fi.xml.resources.lists.ClusterXmlList;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Gabriela Podolnikova
 */
public class ClusterXmlPool implements IClusterPool {
    
    private List<ClusterElement> clusters;

    public ClusterXmlPool(String clusterPoolPath) throws IOException {
        XmlMapper xmlMapper = new XmlMapper();
        String clusterPoolMessage = new String(Files.readAllBytes(Paths.get(clusterPoolPath)));
        ClusterXmlList xmlList = xmlMapper.readValue(clusterPoolMessage, ClusterXmlList.class);
        clusters =  ClusterXmlMapper.map(xmlList.getClusters());
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
