/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.muni.fi.simulator;

import cz.muni.fi.scheduler.elements.VmElement;
import cz.muni.fi.xml.mappers.VmXmlMapper;
import cz.muni.fi.xml.pools.VmXmlPool;
import cz.muni.fi.xml.resources.VmXml;
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
    
    public void runONESimulator(){
        try {
            // Nacitaj xml
            VmXmlPool pool = new VmXmlPool("pools/vmpool.xml");
            
            // nastav CPU=0.5
            VmElement vm = pool.getVm(0);
            vm.setCpu(0.5f);
            
            // premapuj virtualky ktore chces zapisat
            List<VmXml> list = VmXmlMapper.mapToXml(pool.getVms());
            
            // vytvor objekt ktory vie XmlMapper zapisovat (trieda s anotaciou @JacksonXmlRootElement)
            VmXmlList xmlList = new VmXmlList();
            xmlList.setVms(list);
            
            // zapis
            xmlList.writeToFile("pools/vmpool_out.xml");
        } catch (IOException ex) {
            Logger.getLogger(ONESimulator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
