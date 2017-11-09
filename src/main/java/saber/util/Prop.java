package saber.util;

import org.apache.commons.io.IOUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Properties;

/**
 * @author Kahle
 */
public class Prop {
    private static final String DEFAULT_CHARSET_NAME = Charset.defaultCharset().name();
    private static final String STRING_TRUE = "true";
    private static final String STRING_FALSE = "false";

    public static Prop on(Properties properties) {
        return new Prop(properties);
    }

    public static Prop on(String fileName) {
        return on(fileName, DEFAULT_CHARSET_NAME);
    }

    public static Prop on(String fileName, String encoding) {
        InputStream inputStream = null;
        try {
            inputStream = Prop.class.getClassLoader().getResourceAsStream(fileName);
            if (inputStream == null) {
                throw new IllegalArgumentException("Properties file not found in classpath: " + fileName);
            }
            return on(inputStream, encoding);
        }
        catch (IOException e) {
            throw new RuntimeException("Error loading properties file.", e);
        }
        finally {
            IOUtils.closeQuietly(inputStream);
        }
    }

    public static Prop on(File file) {
        return on(file, DEFAULT_CHARSET_NAME);
    }

    public static Prop on(File file, String encoding) {
        if (file == null) {
            throw new IllegalArgumentException("File can not be null.");
        }
        if (!file.isFile()) {
            throw new IllegalArgumentException("Is not file : " + file.getName());
        }
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
            return on(inputStream, encoding);
        }
        catch (IOException e) {
            throw new RuntimeException("Loading properties file error.", e);
        }
        finally {
            IOUtils.closeQuietly(inputStream);
        }
    }

    public static Prop on(InputStream in) throws IOException {
        return on(new InputStreamReader(in, DEFAULT_CHARSET_NAME));
    }

    public static Prop on(InputStream in, String encoding) throws IOException {
        return on(new InputStreamReader(in, encoding));
    }

    public static Prop on(Reader reader) throws IOException {
        Properties properties = new Properties();
        properties.load(reader);
        return new Prop(properties);
    }

    private Properties properties;

    private Prop(Properties properties) {
        this.properties = properties;
    }

    public Properties getProperties() {
        return properties;
    }

    public boolean containsKey(String key) {
        return properties.containsKey(key);
    }

    public String get(String key) {
        return properties.getProperty(key);
    }

    public String get(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    public Integer getInt(String key) {
        return getInt(key, null);
    }

    public Integer getInt(String key, Integer defaultValue) {
        String value = properties.getProperty(key);
        return value != null ? Integer.parseInt(value.trim()) : defaultValue;
    }

    public Long getLong(String key) {
        return getLong(key, null);
    }

    public Long getLong(String key, Long defaultValue) {
        String value = properties.getProperty(key);
        return value != null ? Long.parseLong(value.trim()) : defaultValue;
    }

    public Boolean getBoolean(String key) {
        return getBoolean(key, null);
    }

    public Boolean getBoolean(String key, Boolean defaultValue) {
        String value = properties.getProperty(key);
        if (value != null) {
            value = value.toLowerCase().trim();
            if (STRING_TRUE.equals(value)) {
                return true;
            } else if (STRING_FALSE.equals(value)) {
                return false;
            } else {
                throw new IllegalArgumentException("The value can not parse to Boolean: " + value);
            }
        } else {
            return defaultValue;
        }
    }

}
