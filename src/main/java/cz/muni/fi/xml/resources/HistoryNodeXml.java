/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.xml.resources;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

/**
 *
 * @author Andras Urge
 */
@JacksonXmlRootElement(localName = "HISTORY")
public class HistoryNodeXml {
    
    @JacksonXmlProperty(localName = "RSTIME")
    private long startTime;
    
    @JacksonXmlProperty(localName = "RETIME")
    private long endTime;
    
    @JacksonXmlProperty(localName = "REASON")
    private int reason; 

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public int getReason() {
        return reason;
    }

    public void setReason(int reason) {
        this.reason = reason;
    }

    @Override
    public String toString() {
        return "HistoryNodeXml{" + "startTime=" + startTime + ", endTime=" + endTime + ", reason=" + reason + '}';
    }
}
