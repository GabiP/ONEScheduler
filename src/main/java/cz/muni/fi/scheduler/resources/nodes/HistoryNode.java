/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.scheduler.resources.nodes;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.opennebula.client.PoolElement;

/**
 *
 * @author Andras Urge
 */
public class HistoryNode extends AbstractNode {
    
    private long startTime;
    private long endTime;
    private int runTime;
    
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
    
    public void setReason(int reason) {
        this.reason = reason;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public void setRunTime(int runTime) {
        this.runTime = runTime;
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

    public int getRunTime() {
        // TODO : agree with Dalibor how to handle missing End Time
        if (endTime != 0) {
            runTime = (int) (endTime - startTime);
        } else {
            runTime = (int) (System.currentTimeMillis()/1000L - startTime);
        }
        return runTime;
    }
    
    public boolean isClosed() {
        return (reason != 0);
    }    
    
    // TODO: remove (just for debugging purposes)
    public String formatStartTime() {
        Date date = new Date(startTime*1000L); // *1000 is to convert seconds to milliseconds
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
        return sdf.format(date);
    }        
    
    // TODO: remove (just for debugging purposes)
    public String formatRunTime() {
        int daySecs = 24*3600;
        int hourSecs = 3600;
        int minSecs = 60;
               
        int time = runTime;
        int days = time / daySecs;
        time -= days*daySecs;
        int hours = time / hourSecs;
        time -= hours*hourSecs;
        int minutes = time / minSecs;
        time -= minutes*minSecs;        
        
        return days + "d " + hours+":"+minutes+ ":"+time;
    }

    @Override
    public String toString() {
        return "HistoryNode{" + "startTime=" + startTime + ", endTime=" + endTime + ", runTime=" + runTime + ", reason=" + reason + '}';
    }
    
}
