package cz.muni.fi.scheduler.filters;

/**
 * This class helps to create instances of filters to be used in the system.
 * @author Gabriela Podolnikova
 */
public class FilterFactory {
    /**
     * Create a IFilter instance. The creation is based on using the reflection,
     * hence the caller must have sufficient security privilege etc.
     *
     * @param className
     *            the class name; it must be a valid class name on the classpath;
     *            the class must implement the IFilter interface
     *
     * @return the instance of the filter but not initialized yet
     *
     * @throws RuntimeException
     *             if the filter could not be properly created
     */
    public static IFilter createFilter(String className) {
        try {
            // Make the instance of the filter
            final Class<?> pluginClass = Class.forName(className);
            final Object result = pluginClass.newInstance();

            return (IFilter)result;
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
