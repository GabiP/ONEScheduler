/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.scheduler.core;

import java.util.Date;

/**
 *
 * @author Andras Urge
 */
public class TimeManager {
    
    private static TimeManager instance = null;
    private Date schedulingTimeStamp;
    
    private TimeManager(){
    }
    
    public static TimeManager getInstance() {
        if (instance == null) {
            instance = new TimeManager();
        }
        return instance;
    }

    public Date getSchedulingTimeStamp() {
        return schedulingTimeStamp;
    }

    public void setSchedulingTimeStamp(Date schedulingTimeStamp) {
        this.schedulingTimeStamp = schedulingTimeStamp;
    }
}
