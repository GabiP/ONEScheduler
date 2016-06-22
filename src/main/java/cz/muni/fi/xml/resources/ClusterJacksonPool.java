/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.xml.resources;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

/**
 *
 * @author Gabriela Podolnikova
 */
@JacksonXmlRootElement(localName = "CLUSTER_POOL")
public class ClusterJacksonPool {
    
    @JacksonXmlProperty(localName = "CLUSTER")
    @JacksonXmlElementWrapper(useWrapping = false)
    private ClusterXml[] clusters;

    public ClusterXml[] getClusters() {
        return clusters;
    }

    public void setClusters(ClusterXml[] clusters) {
        this.clusters = clusters;
    }

    @Override
    public String toString() {
        return "ClusterJacksonPool{" + "clusters=" + clusters + '}';
    }
    
}
