/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.scheduler.fairshare.historyrecords;

import cz.muni.fi.scheduler.core.TimeManager;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Andras Urge
 */
public class UserFairshareRecordManager implements IUserFairshareRecordManager {
    
    private final static String LAST_DECAY_KEY = "lastDecay";
    
    private Properties properties = new Properties();
    private File file;

    public UserFairshareRecordManager(String filePath) {
        this.file = new File(filePath);
        if (file.exists()) {
            try (FileInputStream in = new FileInputStream(filePath)) {
                properties.load(in);
            } catch (IOException ex) {
                Logger.getLogger(UserFairshareRecordManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    @Override
    public float getPriority(int userId) {
        String priority = properties.getProperty(Integer.toString(userId), "0");
        return Float.parseFloat(priority);
    }
    
    @Override
    public void storePriority(int userId, float priority) {        
        properties.setProperty(Integer.toString(userId), Float.toString(priority));
        saveToFile();        
    }

    @Override
    public long getLastDecayTime() {
        String lastDecayTime = properties.getProperty(LAST_DECAY_KEY, "0");
        return Long.parseLong(lastDecayTime);
    }

    @Override
    public void applyDecay(int decayValue) {
        for(String key : properties.stringPropertyNames()) {
            if (!key.equals(LAST_DECAY_KEY)) {
                int userId = Integer.parseInt(key);
                float newPriority = getPriority(userId) / decayValue;
                properties.setProperty(key, Float.toString(newPriority));
            }
        }
        long timeStamp = TimeManager.getInstance().getSchedulingTimeStamp().getTime();
        properties.setProperty(LAST_DECAY_KEY, Long.toString(timeStamp));
        saveToFile();
    }
    
    @Override
    public void clearContent() {
        file.delete();
    }
    
    private void saveToFile() {
        try (FileOutputStream fileOut = new FileOutputStream(file)) {
            properties.store(fileOut, "User priorities");
        } catch (IOException ex) {
            Logger.getLogger(UserFairshareRecordManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
