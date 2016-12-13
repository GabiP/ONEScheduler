/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi;

import cz.muni.fi.scheduler.elements.ClusterElement;
import cz.muni.fi.xml.mappers.ClusterXmlMapper;
import cz.muni.fi.xml.pools.ClusterXmlPool;
import cz.muni.fi.xml.resources.ClusterXml;
import cz.muni.fi.xml.resources.lists.ClusterXmlList;
import java.io.IOException;
import java.util.List;
import org.opennebula.client.ClientConfigurationException;


/**
 *
 * @author Andras Urge
 */
public class XmlWriteExample {
    public static void main(String[] args) throws IOException, ClientConfigurationException {
        ClusterXmlPool pool = new ClusterXmlPool("onePoolXml/one_clusterpool.xml");
        ClusterElement c = pool.getCluster(104);
        c.setId(11);
        
        List<ClusterXml> list = ClusterXmlMapper.mapToXml(pool.getClusters());
        ClusterXmlList xmlList = new ClusterXmlList();
        xmlList.setClusters(list);
        
        xmlList.writeToFile("testFile.xml");
    }
    
}
