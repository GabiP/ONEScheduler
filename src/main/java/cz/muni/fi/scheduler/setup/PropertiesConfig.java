package cz.muni.fi.scheduler.setup;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * This class provides access to property files. These files hold the
 * configuration for the scheduler.
 * 
 * @author Gabriela Podolnikova
 */
public class PropertiesConfig {
    
    private final Properties props = new Properties();
    
    public PropertiesConfig(String path) throws IOException {
        InputStream is = new FileInputStream(path);
        props.load(is);
        is.close();
    }
    
    public String getString(String key) {
        String str;
        str = props.getProperty(key);
        return str;
    }
     
    public String[] getStringArray(String key) {
        String value = props.getProperty(key);
        return extractStringArray(value);
    }

    private static String[] extractStringArray(String value) {
        String[] s = value.split("\\s*,\\s*");
        return s;
    }
     
    public boolean getBoolean(String key) {
        String value = props.getProperty(key);
        return extractBoolean(value);
    }

    private static boolean extractBoolean(String value) {
        boolean bool;
        if ((!value.equalsIgnoreCase("true")) && (!value.equalsIgnoreCase("false"))) {
            throw new IllegalArgumentException("Not a boolean: " + value);
        }
        bool = Boolean.parseBoolean(value);
        return bool;
    }
    
    /**
     * Gets int value from properties.
     *
     * @param key the configuration item key
     *
     * @return the value as int
     */
    public int getInt(String key) {
        String value = props.getProperty(key);
        return extractInt(value);
    }

    private static int extractInt(String value) {
        int i;
        i = Integer.parseInt(value);
        return i;
    }
}
