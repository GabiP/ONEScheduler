package cz.muni.fi.xml.resources.nodes;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

/**
 * This class represents the history node retrived from xml.
 * @author Andras Urge
 */
@JacksonXmlRootElement(localName = "HISTORY")
public class HistoryNodeXml {    
    
    @JacksonXmlProperty(localName = "SEQ")
    private int sequence;
    
    @JacksonXmlProperty(localName = "RSTIME")
    private long startTime;
    
    @JacksonXmlProperty(localName = "RETIME")
    private long endTime;
    
    @JacksonXmlProperty(localName = "REASON")
    private int reason; 

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

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
