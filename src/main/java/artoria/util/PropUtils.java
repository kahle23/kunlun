package artoria.util;

import artoria.exception.UncheckedException;
import artoria.io.IOUtils;

import java.io.*;
import java.util.Properties;

import static artoria.util.Const.*;

/**
 * Properties tools.
 * @author Kahle
 */
public class PropUtils {

    public static PropUtils create(String fileName) {
        return PropUtils.create(fileName, DEFAULT_CHARSET_NAME);
    }

    public static PropUtils create(String fileName, String charset) {
        Assert.notBlank(fileName, "File name must is not blank. ");
        Assert.notBlank(charset, "Charset must is not blank. ");
        InputStream inputStream = null;
        try {
            inputStream = IOUtils.findClasspath(fileName);
            if (inputStream == null) {
                throw new IllegalArgumentException("Properties file not found in classpath : " + fileName);
            }
            return PropUtils.create(inputStream, charset);
        }
        catch (IOException e) {
            String msg = "Loading properties file[classpath: " + fileName + "] error. ";
            throw new UncheckedException(msg, e);
        }
        finally {
            IOUtils.closeQuietly(inputStream);
        }
    }

    public static PropUtils create(File dest) {
        return PropUtils.create(dest, DEFAULT_CHARSET_NAME);
    }

    public static PropUtils create(File dest, String charset) {
        Assert.notNull(dest, "Destination must is not null. ");
        Assert.notBlank(charset, "Charset must is not blank. ");
        Assert.state(dest.exists(), "Destination must is exists. ");
        Assert.state(dest.isFile(), "Destination must is a file. ");
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(dest);
            Reader reader = new InputStreamReader(inputStream, charset);
            return PropUtils.create(reader);
        }
        catch (IOException e) {
            String msg = "Loading properties file[" + dest.toString() + "] error. ";
            throw new UncheckedException(msg, e);
        }
        finally {
            IOUtils.closeQuietly(inputStream);
        }
    }

    public static PropUtils create(InputStream in) throws IOException {
        return PropUtils.create(in, DEFAULT_CHARSET_NAME);
    }

    public static PropUtils create(InputStream in, String charset) throws IOException {
        Assert.notNull(in, "InputStream must is not null. ");
        Assert.notBlank(charset, "Charset must is not blank. ");
        Reader reader = new InputStreamReader(in, charset);
        return PropUtils.create(reader);
    }

    public static PropUtils create(Reader reader) throws IOException {
        Assert.notNull(reader, "Reader must is not null. ");
        Properties properties = new Properties();
        properties.load(reader);
        return new PropUtils(properties);
    }

    public static PropUtils create(Properties properties) {
        Assert.notNull(properties, "Properties must is not null. ");
        return new PropUtils(properties);
    }

    public static PropUtils create() {
        return new PropUtils(new Properties());
    }

    private Properties properties;

    private PropUtils(Properties properties) {
        this.properties = properties;
    }

    public Properties getProperties() {
        return properties;
    }

    public boolean containsKey(String key) {
        return properties.containsKey(key);
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    public String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    public Integer getInteger(String key) {
        return this.getInteger(key, null);
    }

    public Integer getInteger(String key, Integer defaultValue) {
        String value = properties.getProperty(key);
        return StringUtils.isNotBlank(value) ? Integer.parseInt(value.trim()) : defaultValue;
    }

    public Long getLong(String key) {
        return this.getLong(key, null);
    }

    public Long getLong(String key, Long defaultValue) {
        String value = properties.getProperty(key);
        return StringUtils.isNotBlank(value) ? Long.parseLong(value.trim()) : defaultValue;
    }

    public Boolean getBoolean(String key) {
        return this.getBoolean(key, null);
    }

    public Boolean getBoolean(String key, Boolean defaultValue) {
        String value = properties.getProperty(key);
        if (StringUtils.isBlank(value)) {
            return defaultValue;
        }
        value = value.toLowerCase().trim();
        if (TRUE.equals(value)) {
            return true;
        }
        else if (FALSE.equals(value)) {
            return false;
        }
        else {
            throw new IllegalArgumentException("The value can not parse to Boolean : " + value);
        }
    }

}
