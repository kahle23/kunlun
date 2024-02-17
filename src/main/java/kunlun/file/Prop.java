/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.file;

import kunlun.exception.ExceptionUtils;
import kunlun.io.util.IOUtils;
import kunlun.io.util.StringBuilderWriter;
import kunlun.util.Assert;
import kunlun.util.MapUtils;
import kunlun.util.StringUtils;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Properties file.
 * @author Kahle
 */
public class Prop extends TextFile {
    private Properties properties;

    public Prop() {

        this(new Properties());
    }

    public Prop(Properties properties) {

        setProperties(properties);
    }

    public Properties getProperties() {

        return properties;
    }

    public void setProperties(Properties properties) {
        Assert.notNull(properties
                , "Parameter \"properties\" must not null. ");
        this.properties = properties;
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

    public Object setProperty(String key, String value) {

        return properties.setProperty(key, value);
    }

    @Override
    public InputStream getInputStream() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        properties.store(byteArrayOutputStream, null);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return new ByteArrayInputStream(byteArray);
    }

    @Override
    public long read(Reader reader) throws IOException {
        Assert.notNull(reader, "Parameter \"reader\" must not null. ");
        String data = IOUtils.toString(reader);
        properties.load(new StringReader(data));
        return data.length();
    }

    @Override
    public void write(Writer writer) throws IOException {
        Assert.notNull(writer, "Parameter \"writer\" must not null. ");
        properties.store(writer, null);
    }

    public Map<String, Object> toMap() {
        Map<String, Object> result =
                new HashMap<String, Object>(properties.size());
        for (String name : properties.stringPropertyNames()) {
            if (StringUtils.isBlank(name)) { continue; }
            String val = properties.getProperty(name);
            result.put(name, val);
        }
        return result;
    }

    public void fromMap(Map<String, Object> map) {
        properties.clear();
        if (MapUtils.isEmpty(map)) { return; }
        properties.putAll(map);
    }

    @Override
    public String toString() {
        try {
            StringBuilderWriter writer = new StringBuilderWriter();
            properties.store(writer, null);
            return writer.toString();
        }
        catch (IOException e) {
            throw ExceptionUtils.wrap(e);
        }
    }

}
