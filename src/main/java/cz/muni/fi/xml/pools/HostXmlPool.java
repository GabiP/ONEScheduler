/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.xml.pools;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import cz.muni.fi.scheduler.elementpools.IHostPool;
import cz.muni.fi.scheduler.resources.HostElement;
import cz.muni.fi.xml.mappers.HostXmlMapper;
import cz.muni.fi.xml.resources.HostJacksonPool;
import cz.muni.fi.xml.resources.HostXml;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author gabi
 */
public class HostXmlPool implements IHostPool {
    
    private String xml;
    
    private XmlMapper xmlMapper;

    public HostXmlPool(String xml) {
        this.xml = xml;
        xmlMapper = new XmlMapper();
    }    

    @Override
    public List<HostElement> getHosts() {
        HostJacksonPool hostpool = null;
        try {
            hostpool = xmlMapper.readValue(xml, HostJacksonPool.class);
        } catch (IOException ex) {
            Logger.getLogger(HostXmlPool.class.getName()).log(Level.SEVERE, null, ex);
        }
        return HostXmlMapper.map(hostpool.getHosts());
    }

    @Override
    public List<HostElement> getActiveHosts() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Integer> getHostsIds() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public HostElement getHost(int id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
