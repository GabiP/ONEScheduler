package cz.muni.fi.scheduler.elements.nodes;

import org.opennebula.client.PoolElement;

/**
 * This class represents a HistoryNode.
 * A virtual machine can have multiple history nodes.
 * Loads the data from xml by using OpenNebula's API.
 * 
 * @author Andras Urge
 */
public class HistoryNode extends AbstractNode {
    
    private int sequence;
    
    private long startTime;
    
    private long endTime;
    
    /**
     * REASON values:
     *    NONE  = 0 History record is not closed yet
     *    ERROR = 1 History record was closed because of an error
     *    USER  = 2 History record was closed because of a user action                    
     */
    private int reason;    
        
    /**
     * Loads the attributes of the HistoryNode from the provided PooleElement.
     * 
     * @param vm the element containing the required data
     * @param xpathExpr the xpath expression to the HistoryNode inside the element
     */
    @Override
    public void load(PoolElement vm, String xpathExpr) {
        sequence = Integer.parseInt(vm.xpath(xpathExpr + "/SEQ"));
        startTime = Integer.parseInt(vm.xpath(xpathExpr + "/RSTIME"));
        endTime = Integer.parseInt(vm.xpath(xpathExpr + "/RETIME"));
        reason = Integer.parseInt(vm.xpath(xpathExpr + "/REASON"));        
    }  
    
    /**
     * A history is closed if it contains a valid start time and end time.
     * This should happen when the virtual machine finishes executing during
     * the given period.
     * 
     * @return whether the history is closed
     */
    public boolean isClosed() {
        return (startTime > 0) && (endTime > 0) && (endTime > startTime);
    }   

    public void setSequence(int sequence) {
        this.sequence = sequence;
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
    
    public int getSequence() {
        return sequence;
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
