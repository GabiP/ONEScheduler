/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.xml.resources.lists;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import cz.muni.fi.xml.resources.ClusterXml;
import java.util.List;

/**
 *
 * @author Andras Urge
 */
@JacksonXmlRootElement(localName = "CLUSTERPOOL")
public class ClusterXmlList {
    
    @JacksonXmlProperty(localName = "CLUSTER")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<ClusterXml> clusters;

    public List<ClusterXml> getClusters() {
        return clusters;
    }

    public void setClusters(List<ClusterXml> clusters) {
        this.clusters = clusters;
    }
}