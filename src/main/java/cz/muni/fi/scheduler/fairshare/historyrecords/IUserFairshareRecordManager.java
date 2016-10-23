/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.scheduler.fairshare.historyrecords;

/**
 *
 * @author Andras Urge
 */
public interface IUserFairshareRecordManager {
    
    public float getPriority(int userId);
    
    public void storePriority(int userId, float priority);
}
