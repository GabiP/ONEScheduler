/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.scheduler.setup;

import cz.muni.fi.exceptions.LoadingFailedException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Andras Urge
 */
public class FairshareConfiguration {
            
    private static final String PENALTY_FUNCTION = "penaltyFunction";
    private static final String CPU_WEIGHT = "cpuWeight";
    private static final String RAM_WEIGHT = "ramWeight";
    private static final String HDD_WEIGHT = "hddWeight";  
        
    private String path;
    
    private String penaltyFunction;
    
    private int decayValue;
    private int decayInterval;
    
    private float cpuWeight;
    private float ramWeight;
    private float hddWeight;
    
    private Map<Integer, Float> userPercentages = new HashMap<>();
    private Map<Integer, Float> userGroupPercentages = new HashMap<>();
    

    public FairshareConfiguration(String path) throws LoadingFailedException {
        try {
            PropertiesConfig properties = new PropertiesConfig(path);
            this.path = path;
            penaltyFunction = properties.getString(PENALTY_FUNCTION);
            decayValue = properties.getInt("decayValue");
            decayInterval = properties.getInt("decayInterval");
            loadWeights(properties);
            loadFairsharePercentages(properties);
        } catch (IOException ex) {
            throw new LoadingFailedException(ex.toString());
        }
    }

    private void loadWeights(PropertiesConfig properties) throws LoadingFailedException {
        cpuWeight = properties.getFloat(CPU_WEIGHT);
        ramWeight = properties.getFloat(RAM_WEIGHT);
        hddWeight = properties.getFloat(HDD_WEIGHT);
         
        Float[] resourceWeights = {cpuWeight, ramWeight, hddWeight};
        
        float weightSum = 0;
        for (float weight : resourceWeights) {
            if (weight < 0) {
                throw new LoadingFailedException("Incorrect fairshare configuration in " + path + " - resource weights cannot be smaller than 0.");
            }
            weightSum += weight;
        }
        if (weightSum == 0) {
            throw new LoadingFailedException("Incorrect fairshare configuration in " + path + " - at least one resource weight needs to be bigger than 0.");
        }  
    }

    private void loadFairsharePercentages(PropertiesConfig properties) {
        for (String key : properties.keySet()) {
            String[] parts = key.split("-");
            switch (parts[0]) {
                case "user":
                    int userId = Integer.parseInt(parts[1]);
                    userPercentages.put(userId, properties.getFloat(key));
                    break;
                case "userGroup":
                    int groupId = Integer.parseInt(parts[1]);
                    userGroupPercentages.put(groupId, properties.getFloat(key));
                    break;
            }
        }
    }
    
    public String getPath() {
        return path;
    }
    
    public String getPenaltyFunction() {
        return penaltyFunction;
    }

    public int getDecayValue() {
        return decayValue;
    }

    public int getDecayInterval() {
        return decayInterval;
    }

    public float getCpuWeight() {
        return cpuWeight;
    }

    public float getRamWeight() {
        return ramWeight;
    }

    public float getHddWeight() {
        return hddWeight;
    }    

    public Map<Integer, Float> getUserPercentages() {
        return userPercentages;
    }

    public Map<Integer, Float> getUserGroupPercentages() {
        return userGroupPercentages;
    }
}
