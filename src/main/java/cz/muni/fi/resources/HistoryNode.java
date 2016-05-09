/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.resources;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.opennebula.client.PoolElement;

/**
 *
 * @author Andras Urge
 */
public class HistoryNode extends NodeElement {

    private static final String XPATH_EXPR = "/VM/HISTORY_RECORDS/HISTORY";
    
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
    void load(PoolElement vm, int index) {
        startTime = Integer.parseInt(vm.xpath(XPATH_EXPR+"["+index+"]" + "/RSTIME"));
        endTime = Integer.parseInt(vm.xpath(XPATH_EXPR+"["+index+"]" + "/RETIME"));
        reason = Integer.parseInt(vm.xpath(XPATH_EXPR+"["+index+"]" + "/REASON"));
        if (endTime != 0) {
            runTime = (int) (endTime - startTime);
        } else {
            runTime = (int) (System.currentTimeMillis()/1000L - startTime);
        }
    }

    @Override
    String getXpathExpr() {
        return XPATH_EXPR;
    }    
    
    public long getStartTime() {
        return startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public int getRunTime() {
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
    
}
