package apyh.artoria.serialize;

import apyh.artoria.util.ArrayUtils;
import apyh.artoria.util.Assert;

import java.io.*;

/**
 * Serialize tools.
 * @author Kahle
 */
public class SerializeUtils {
    private static final int INITIAL_SIZE = 128;
    private static Serializer serializer;
    private static Deserializer deserializer;

    static {
        SerializeUtils.serializer = new JavaSerializer();
        SerializeUtils.deserializer = new JavaDeserializer();
    }

    public static void setSerializer(Serializer serializer) {
        Assert.notNull(serializer, "Serializer must is not null. ");
        SerializeUtils.serializer = serializer;
    }

    public static void setDeserializer(Deserializer deserializer) {
        Assert.notNull(deserializer, "Deserializer must is not null. ");
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
        Assert.state(ArrayUtils.isNotEmpty(bytes), "Byte array must is not empty. ");
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
            return SerializeUtils.deserialize(bais);
        }
        catch (IOException e) {
            throw new IllegalArgumentException("Failed to deserialize object. ", e);
        }
        catch (ClassNotFoundException e) {
            throw new IllegalStateException("Failed to deserialize object type. ", e);
        }
    }

    public static void serialize(Object object, OutputStream outputStream) throws IOException {
        serializer.serialize(object, outputStream);
    }

    public static Object deserialize(InputStream inputStream) throws IOException, ClassNotFoundException {
        return deserializer.deserialize(inputStream);
    }

}
