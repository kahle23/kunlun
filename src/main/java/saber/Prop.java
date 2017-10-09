package saber;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Properties;

public class Prop {
    private static final String DEFAULT_CHARSET_NAME = Charset.defaultCharset().name();

    private Properties properties;

    public Prop(Properties properties) {
        this.properties = properties;
    }

    public Prop(String fileName) {
        this(fileName, DEFAULT_CHARSET_NAME);
    }

    public Prop(String fileName, String encoding) {
        InputStream inputStream = null;
        try {
            inputStream = IOUtils.findClasspath(fileName);
            if (inputStream == null) {
                throw new IllegalArgumentException("Properties file not found in classpath: " + fileName);
            }
            properties = new Properties();
            properties.load(new InputStreamReader(inputStream, encoding));
        } catch (IOException e) {
            throw new RuntimeException("Error loading properties file.", e);
        } finally {
            ReleaseUtils.closeQuietly(inputStream);
        }
    }

    public Prop(File file) {
        this(file, DEFAULT_CHARSET_NAME);
    }

    public Prop(File file, String encoding) {
        if (file == null) {
            throw new IllegalArgumentException("File can not be null.");
        }
        if (!file.isFile()) {
            throw new IllegalArgumentException("File is not file : " + file.getName());
        }
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
            properties = new Properties();
            properties.load(new InputStreamReader(inputStream, encoding));
        } catch (IOException e) {
            throw new RuntimeException("Error loading properties file.", e);
        } finally {
            ReleaseUtils.closeQuietly(inputStream);
        }
    }

    public Prop(InputStream in) {
        this(in, DEFAULT_CHARSET_NAME);
    }

    public Prop(InputStream in, String encoding) {
        try {
            properties = new Properties();
            properties.load(new InputStreamReader(in, encoding));
        } catch (IOException e) {
            throw new RuntimeException("Error loading properties file.", e);
        }
    }

    public Prop(Reader reader) {
        try {
            properties = new Properties();
            properties.load(reader);
        } catch (IOException e) {
            throw new RuntimeException("Error loading properties file.", e);
        }
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
            if ("true".equals(value)) {
                return true;
            } else if ("false".equals(value)) {
                return false;
            } else {
                throw new IllegalArgumentException("The value can not parse to Boolean: " + value);
            }
        } else {
            return defaultValue;
        }
    }

}
