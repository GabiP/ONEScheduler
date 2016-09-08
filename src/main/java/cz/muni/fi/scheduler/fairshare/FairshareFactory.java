/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.muni.fi.scheduler.fairshare;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Gabriela Podolnikova
 */
public class FairshareFactory {
    /**
     * Create a IUserPriorityCalculator instance. The creation is based on using the reflection,
     * hence the caller must have sufficient security privilege etc.
     *
     * @param className
     *            the class name; it must be a valid class name on the classpath;
            the class must implement the IUserPriorityCalculator interface
     *
     * @return the instance of the policy but not initialized yet
     *
     * @throws RuntimeException
     *             if the policy could not be properly created
     */
    public static IUserPriorityCalculator createFairshare(String className) {
        try {
            // Make the instance of the filter
            final Class<?> pluginClass = Class.forName(className);
            final Object result = pluginClass.newInstance();

            return (IUserPriorityCalculator)result;
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
    
    public static List<IUserPriorityCalculator> getFairshares(String[] stringfairshares) {
        List<IUserPriorityCalculator> fairshares = new ArrayList<>();
        for (int i = 0; i < stringfairshares.length; i++) {
            String fairshareString = stringfairshares[i];
            if (!fairshareString.contains(".")) {
                fairshareString = "cz.muni.fi.scheduler.fairshare." + fairshareString;
            }
            IUserPriorityCalculator f = FairshareFactory.createFairshare(fairshareString);
            fairshares.add(f);
        }
        return fairshares;
    }
}
