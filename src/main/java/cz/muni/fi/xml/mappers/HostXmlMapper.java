/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.xml.mappers;

import cz.muni.fi.scheduler.resources.HostElement;
import cz.muni.fi.xml.resources.HostXml;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author gabi
 */
public class HostXmlMapper {
    
    public List<HostElement> map(HostXml[] hosts) {
        List<HostElement> result = new ArrayList<>();
        for(HostXml xml : hosts) {
            result.add(map(xml));
        }        
        return result;
    }
    
    public HostElement map(HostXml host) {
        HostElement h = new HostElement();
        h.setId(host.getId());
        
        return h;
    }
}
