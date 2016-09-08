/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.muni.fi.scheduler.policies;

import cz.muni.fi.scheduler.filters.FilterFactory;
import cz.muni.fi.scheduler.filters.hosts.IHostFilter;
import cz.muni.fi.scheduler.policies.datastores.IStoragePolicy;
import cz.muni.fi.scheduler.policies.hosts.IPlacementPolicy;
import java.util.ArrayList;
import java.util.List;

/**
 * This class helps to create instances of policies for scheduling.
 * @author Gabriela Podolnikova
 */
public class PolicyFactory {

    /**
     * Create a IPlacementPolicy instance. The creation is based on using the reflection,
     * hence the caller must have sufficient security privilege etc.
     *
     * @param className
     *            the class name; it must be a valid class name on the classpath;
            the class must implement the IPlacementPolicy interface
     *
     * @return the instance of the policy but not initialized yet
     *
     * @throws RuntimeException
     *             if the policy could not be properly created
     */
    public static IPlacementPolicy createPlacementPolicy(String className) {
        try {
            // Make the instance of the filter
            final Class<?> pluginClass = Class.forName(className);
            final Object result = pluginClass.newInstance();

            return (IPlacementPolicy)result;
        } catch (SecurityException e) {
            throw new RuntimeException("Could not create policy: " + className, e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Could not create policy: " + className, e);
        } catch (ExceptionInInitializerError e) {
            throw new RuntimeException("Could not create policy: " + className, e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Could not create policy: " + className, e);
        } catch (InstantiationException e) {
            throw new RuntimeException("Could not create policy: " + className, e);
        }
    }
    
    public static IStoragePolicy createStoragePolicy(String className) {
        try {
            // Make the instance of the filter
            final Class<?> pluginClass = Class.forName(className);
            final Object result = pluginClass.newInstance();

            return (IStoragePolicy)result;
        } catch (SecurityException e) {
            throw new RuntimeException("Could not create policy: " + className, e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Could not create policy: " + className, e);
        } catch (ExceptionInInitializerError e) {
            throw new RuntimeException("Could not create policy: " + className, e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Could not create policy: " + className, e);
        } catch (InstantiationException e) {
            throw new RuntimeException("Could not create policy: " + className, e);
        }
    }
    
    public static List<IPlacementPolicy> getPlacementPolicies(String[] stringpolicies) {
        List<IPlacementPolicy> policies = new ArrayList<>();
        for (int i = 0; i < stringpolicies.length; i++) {
            String placementString = stringpolicies[i];
            if (!placementString.contains(".")) {
                placementString = "cz.muni.fi.scheduler.policies.hosts." + placementString;
            }
            IPlacementPolicy p = PolicyFactory.createPlacementPolicy(placementString);
            policies.add(p);
        }
        return policies;
    }
    
    public static List<IStoragePolicy> getStoragePolicies(String[] stringpolicies) {
        List<IStoragePolicy> policies = new ArrayList<>();
        for (int i = 0; i < stringpolicies.length; i++) {
            String storageString = stringpolicies[i];
            if (!storageString.contains(".")) {
                storageString = "cz.muni.fi.scheduler.policies.datastores." + storageString;
            }
            IStoragePolicy p = PolicyFactory.createStoragePolicy(storageString);
            policies.add(p);
        }
        return policies;
    }
    
}
