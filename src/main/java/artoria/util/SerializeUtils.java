package artoria.util;

import java.io.*;

/**
 * @author Kahle
 */
public class SerializeUtils {

    public static Object deserialize(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        try {
            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes));
            return ois.readObject();
        }
        catch (IOException e) {
            throw new IllegalArgumentException("Failed to deserialize object", e);
        }
        catch (ClassNotFoundException e) {
            throw new IllegalStateException("Failed to deserialize object type", e);
        }
    }

    public static byte[] serialize(Object object) {
        if (object == null) {
            return null;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);
        try {
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(object);
            oos.flush();
        }
        catch (IOException e) {
            throw new IllegalArgumentException("Failed to serialize object of type: " + object.getClass(), e);
        }
        return baos.toByteArray();
    }

    public static Object deserialize(InputStream inputStream)
            throws IOException {
        ObjectInputStream ois = new ObjectInputStream(inputStream);
        try {
            return ois.readObject();
        }
        catch (ClassNotFoundException e) {
            throw new IllegalStateException("Failed to deserialize object type", e);
        }
    }

    public static void serialize(Object object, OutputStream outputStream) throws IOException {
        Assert.isInstanceOf(Serializable.class, object
                , SerializeUtils.class.getSimpleName()
                        + " requires a Serializable payload "
                        + "but received an object of type ["
                        + object.getClass().getName() + "]");
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        objectOutputStream.writeObject(object);
        objectOutputStream.flush();
    }

}
