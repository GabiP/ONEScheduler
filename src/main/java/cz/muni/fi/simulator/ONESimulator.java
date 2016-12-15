/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.muni.fi.simulator;

import cz.muni.fi.scheduler.elements.HostElement;
import cz.muni.fi.scheduler.elements.VmElement;
import cz.muni.fi.scheduler.setup.PropertiesConfig;
import cz.muni.fi.xml.mappers.HostXmlMapper;
import cz.muni.fi.xml.mappers.VmXmlMapper;
import cz.muni.fi.xml.pools.HostXmlPool;
import cz.muni.fi.xml.pools.VmXmlPool;
import cz.muni.fi.xml.resources.HostXml;
import cz.muni.fi.xml.resources.VmXml;
import cz.muni.fi.xml.resources.lists.HostXmlList;
import cz.muni.fi.xml.resources.lists.VmXmlList;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author dalibor
 */
public class ONESimulator {
    private PropertiesConfig properties;
    
    public void runONESimulator(){
        try {
            properties = new PropertiesConfig("configuration.properties");
            // Nacitaj xml
            VmXmlPool VMpool = new VmXmlPool(properties.getString("vmpoolpath"));
            HostXmlPool hostPool = new HostXmlPool(properties.getString("hostpoolpath"));
            
            // nastav CPU=0.5
            VmElement vm = VMpool.getVm(0);
            vm.setCpu(0.5f);
            
            HostElement host = hostPool.getHost(0);
                        
            // premapuj virtualky ktore chces zapisat
            List<VmXml> VMlist = VmXmlMapper.mapToXml(VMpool.getVms());
            List<HostXml> HostList = HostXmlMapper.mapToXml(hostPool.getHosts());
            
            // vytvor objekt ktory vie XmlMapper zapisovat (trieda s anotaciou @JacksonXmlRootElement)
            VmXmlList VMxmlList = new VmXmlList();
            VMxmlList.setVms(VMlist);
            
            HostXmlList HostxmlList = new HostXmlList();
            HostxmlList.setHosts(HostList);
            
            // zapis
            VMxmlList.writeToFile("pools/vmpool_out.xml");
            HostxmlList.writeToFile("pools/hostpool_out.xml");
        } catch (IOException ex) {
            Logger.getLogger(ONESimulator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
