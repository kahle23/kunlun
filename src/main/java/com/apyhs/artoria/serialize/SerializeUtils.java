package com.apyhs.artoria.serialize;

import com.apyhs.artoria.logging.Logger;
import com.apyhs.artoria.logging.LoggerFactory;
import com.apyhs.artoria.util.Assert;

import java.io.*;

/**
 * Serialize tools.
 * @author Kahle
 */
public class SerializeUtils {
    private static final Logger log = LoggerFactory.getLogger(SerializeUtils.class);
    private static final int INITIAL_SIZE = 128;
    private static Serializer<Object> serializer;
    private static Deserializer<Object> deserializer;

    static {
        SerializeUtils.setSerializer(new DefaultSerializer());
        SerializeUtils.setDeserializer(new DefaultDeserializer());
    }

    public static Serializer<Object> getSerializer() {
        return serializer;
    }

    public static void setSerializer(Serializer<Object> serializer) {
        Assert.notNull(serializer, "Serializer must is not null. ");
        log.info("Set serializer: " + serializer.getClass().getName());
        SerializeUtils.serializer = serializer;
    }

    public static Deserializer<Object> getDeserializer() {
        return deserializer;
    }

    public static void setDeserializer(Deserializer<Object> deserializer) {
        Assert.notNull(deserializer, "Deserializer must is not null. ");
        log.info("Set deserializer: " + deserializer.getClass().getName());
        SerializeUtils.deserializer = deserializer;
    }

    public static byte[] serialize(Object object) {
        Assert.notNull(object, "Object must is not null. ");
        ByteArrayOutputStream baos = new ByteArrayOutputStream(INITIAL_SIZE);
        try {
            SerializeUtils.serialize(object, baos);
        }
        catch (IOException e) {
            String msg = "Failed to serialize object of type : ";
            throw new IllegalArgumentException(msg + object.getClass(), e);
        }
        return baos.toByteArray();
    }

    public static Object deserialize(byte[] bytes) {
        Assert.notEmpty(bytes, "Byte array must is not empty. ");
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
            return SerializeUtils.deserialize(bais);
        }
        catch (IOException e) {
            throw new IllegalArgumentException("Failed to deserialize object. ", e);
        }
    }

    public static void serialize(Object object, OutputStream outputStream) throws IOException {
        Assert.notNull(object, "Object must is not null. ");
        Assert.notNull(outputStream, "OutputStream must is not null. ");
        serializer.serialize(object, outputStream);
    }

    public static Object deserialize(InputStream inputStream) throws IOException {
        Assert.notNull(inputStream, "InputStream must is not null. ");
        return deserializer.deserialize(inputStream);
    }

}
