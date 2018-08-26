package artoria.util;

import artoria.exception.ExceptionUtils;
import artoria.io.IOUtils;

import java.io.*;
import java.util.Properties;

import static artoria.common.Constants.DEFAULT_CHARSET_NAME;

/**
 * Properties tools.
 * @author Kahle
 */
public class PropertiesUtils {

    public static Properties create(String fileName) {
        // Use default charset.
        return PropertiesUtils.create(fileName, DEFAULT_CHARSET_NAME);
    }

    public static Properties create(String fileName, String charset) {
        Assert.notBlank(fileName, "Parameter \"fileName\" must not blank. ");
        Assert.notBlank(charset, "Parameter \"charset\" must not blank. ");
        InputStream inputStream = null;
        try {
            inputStream = IOUtils.findClasspath(fileName);
            if (inputStream == null) {
                throw new IllegalArgumentException("Properties file not found in classpath : " + fileName);
            }
            return PropertiesUtils.create(inputStream, charset);
        }
        catch (IOException e) {
            throw ExceptionUtils.wrap(e);
        }
        finally {
            IOUtils.closeQuietly(inputStream);
        }
    }

    public static Properties create(File dest) {
        // Use default charset.
        return PropertiesUtils.create(dest, DEFAULT_CHARSET_NAME);
    }

    public static Properties create(File destination, String charset) {
        Assert.notNull(destination, "Parameter \"destination\" must not null. ");
        Assert.notBlank(charset, "Parameter \"charset\" must not blank. ");
        Assert.state(destination.exists(), "Parameter \"destination\" must be exists. ");
        Assert.state(destination.isFile(), "Parameter \"destination\" must be a file. ");
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(destination);
            Reader reader = new InputStreamReader(inputStream, charset);
            return PropertiesUtils.create(reader);
        }
        catch (IOException e) {
            throw ExceptionUtils.wrap(e);
        }
        finally {
            IOUtils.closeQuietly(inputStream);
        }
    }

    public static Properties create(InputStream inputStream) throws IOException {
        // Use default charset.
        return PropertiesUtils.create(inputStream, DEFAULT_CHARSET_NAME);
    }

    public static Properties create(InputStream inputStream, String charset) throws IOException {
        Assert.notNull(inputStream, "Parameter \"inputStream\" must not null. ");
        Assert.notBlank(charset, "Parameter \"charset\" must not blank. ");
        Reader reader = new InputStreamReader(inputStream, charset);
        return PropertiesUtils.create(reader);
    }

    public static Properties create(Reader reader) throws IOException {
        Assert.notNull(reader, "Parameter \"reader\" must not null. ");
        Properties properties = new Properties();
        properties.load(reader);
        return properties;
    }

}
