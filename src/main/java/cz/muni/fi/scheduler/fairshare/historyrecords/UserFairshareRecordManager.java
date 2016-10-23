/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.scheduler.fairshare.historyrecords;

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
    
    private Properties properties = new Properties();
    private String filePath;

    public UserFairshareRecordManager(String filePath) {
        this.filePath = filePath;
        try (FileInputStream in = new FileInputStream(filePath)) {
            properties.load(in);
        } catch (IOException ex) {
            Logger.getLogger(UserFairshareRecordManager.class.getName()).log(Level.SEVERE, null, ex);
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

        File file = new File(filePath);
        try (FileOutputStream fileOut = new FileOutputStream(file)) {
            properties.store(fileOut, "User priorities");
        } catch (IOException ex) {
            Logger.getLogger(UserFairshareRecordManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
