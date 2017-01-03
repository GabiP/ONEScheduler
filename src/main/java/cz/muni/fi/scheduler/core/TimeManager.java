package cz.muni.fi.scheduler.core;

import java.util.Date;

/**
 * A singleton class used for managing scheduling related time information.
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
