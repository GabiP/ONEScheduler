/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.filters;

/**
 * Class FilterFactory is used for creating Filter instances.
 * 
 * @author Gabriela Podolnikova
 */
public class FilterFactory {
    /**
     * Create a Filter instance. The creation is based on using the reflection,
     * hence the caller must have sufficient security privilege etc.
     *
     * @param className
     *            the class name; it must be a valid class name on the classpath;
     *            the class must implement the Filter interface
     *
     * @return the instance of the filter, but not initialized yet
     *
     * @throws RuntimeException
     *             if the filter could not be properly created
     */
    public static Filter createFilter(String className) {
        try {
            // Make the instance of the plugin
            final Class<?> pluginClass = Class.forName(className);
            final Object result = pluginClass.newInstance();

            return (Filter)result;
        } catch (SecurityException e) {
            throw new RuntimeException("Could not create filter: " + className, e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Could not create filter: " + className, e);
        } catch (ExceptionInInitializerError e) {
            throw new RuntimeException("Could not create filter: " + className, e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Could not create filter: " + className, e);
        } catch (InstantiationException e) {
            throw new RuntimeException("Could not create filter: " + className, e);
        }
    }
}
