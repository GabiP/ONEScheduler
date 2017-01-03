/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.scheduler.fairshare.historyrecords;

/**
 * Interface for storing the so far calculated user priorities and applying the
 * decay on them.
 * 
 * @author Andras Urge
 */
public interface IUserFairshareRecordManager {
    
    /**
     * Returns the fairshare priority stored for the user.
     * 
     * @param userId the ID of the user
     * @return the stored fairshare priority of the user
     */
    float getPriority(int userId);
    
    /**
     * Store the fairshare priority of the given user.
     * 
     * @param userId the ID of the user
     * @param priority the fairshare priority to store
     */
    void storePriority(int userId, float priority);
    
    /**
     * Returns the last time when decay was applied on the stored fairshare priorities.
     * 
     * @return the last time when decay was applied
     */
    long getLastDecayTime();
    
    /**
     * Divides all the stored fairshare priorities with the given decay value.
     * 
     * @param decayValue the value used to divide the priorities
     */
    void applyDecay(int decayValue);
    
    /**
     * Removes all the stored fairshare priorities.
     */
    void clearContent();
}
