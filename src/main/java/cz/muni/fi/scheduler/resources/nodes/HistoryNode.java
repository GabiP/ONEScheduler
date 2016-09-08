package cz.muni.fi.scheduler.resources.nodes;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.opennebula.client.PoolElement;

/**
 * This class represents a HistoryNode.
 * A virtual machine can have multiple history nodes.
 * Loads the data from xml by using OpenNebula's API.
 * 
 * @author Andras Urge
 */
public class HistoryNode extends AbstractNode {
    
    private long startTime;
    private long endTime;
    
    /**
     * REASON values:
     *    NONE  = 0 History record is not closed yet
     *    ERROR = 1 History record was closed because of an error
     *    USER  = 2 History record was closed because of a user action                    
     */
    private int reason;    
        
    @Override
    public void load(PoolElement vm, String xpathExpr) {
        startTime = Integer.parseInt(vm.xpath(xpathExpr + "/RSTIME"));
        endTime = Integer.parseInt(vm.xpath(xpathExpr + "/RETIME"));
        reason = Integer.parseInt(vm.xpath(xpathExpr + "/REASON"));        
    }  
    
    public boolean isClosed() {
        return (startTime > 0) && (endTime > 0) && (endTime > startTime);
    }   
    
    public void setReason(int reason) {
        this.reason = reason;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }
    
    public int getReason() {
        return reason;
    }
    
    public long getStartTime() {
        return startTime;
    }

    public long getEndTime() {
        return endTime;
    }    
    
    @Override
    public String toString() {
        return "HistoryNode{" + "startTime=" + startTime + ", endTime=" + endTime + ", reason=" + reason + '}';
    }    
}
