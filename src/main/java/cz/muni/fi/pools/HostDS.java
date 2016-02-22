/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.pools;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

/**
 *
 * @author Gabriela Podolnikova
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class HostDS {
    
    @JacksonXmlProperty(localName = "ID")
    private Integer id;
    
    @JacksonXmlProperty(localName = "FREE_MB")
    private Integer free_mb;
    
    @JacksonXmlProperty(localName = "TOTAL_MB")
    private Integer total_mb;
    
    @JacksonXmlProperty(localName = "USED_MB")
    private Integer used_mb;
}
