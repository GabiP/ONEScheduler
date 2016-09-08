package cz.muni.fi.scheduler.filters;

import cz.muni.fi.scheduler.filters.datastores.IDatastoreFilter;
import cz.muni.fi.scheduler.filters.hosts.IHostFilter;
import java.util.ArrayList;
import java.util.List;

/**
 * This class helps to create instances of filters to be used in the system.
 * @author Gabriela Podolnikova
 */
public class FilterFactory {
    /**
     * Create a IHostFilter instance. The creation is based on using the reflection,
     * hence the caller must have sufficient security privilege etc.
     *
     * @param className
     *            the class name; it must be a valid class name on the classpath;
            the class must implement the IHostFilter interface
     *
     * @return the instance of the filter but not initialized yet
     *
     * @throws RuntimeException
     *             if the filter could not be properly created
     */
    public static IHostFilter createHostFilter(String className) {
        try {
            // Make the instance of the filter
            final Class<?> pluginClass = Class.forName(className);
            final Object result = pluginClass.newInstance();

            return (IHostFilter)result;
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
    
    public static IDatastoreFilter createDatastoreFilter(String className) {
        try {
            // Make the instance of the filter
            final Class<?> pluginClass = Class.forName(className);
            final Object result = pluginClass.newInstance();

            return (IDatastoreFilter)result;
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
    
    public static List<IHostFilter> getHostFilters(String[] stringfilters) {
        List<IHostFilter> filters = new ArrayList<>();
        for (int i = 0; i < stringfilters.length; i++) {
            String filterString = stringfilters[i];
            if (!filterString.contains(".")) {
                filterString = "cz.muni.fi.scheduler.filters.hosts." + filterString;
            }
            IHostFilter f = FilterFactory.createHostFilter(filterString);
            filters.add(f);
        }
        return filters;
    }
    
    public static List<IDatastoreFilter> getDatastoreFilters(String[] stringfilters) {
        List<IDatastoreFilter> filters = new ArrayList<>();
        for (int i = 0; i < stringfilters.length; i++) {
            String filterString = stringfilters[i];
            if (!filterString.contains(".")) {
                filterString = "cz.muni.fi.scheduler.filters.hosts." + filterString;
            }
            IDatastoreFilter f = FilterFactory.createDatastoreFilter(filterString);
            filters.add(f);
        }
        return filters;
    }
}
