package artoria.util;

import java.io.*;

/**
 * Serialize tools.
 * @author Kahle
 */
public class SerializeUtils {
    private static final int INITIAL_SIZE = 128;

    public static Object deserialize(byte[] bytes) {
        Assert.state(ArrayUtils.isNotEmpty(bytes), "Bytes must is not empty. ");
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(bais);
            return ois.readObject();
        }
        catch (IOException e) {
            throw new IllegalArgumentException("Failed to deserialize object. ", e);
        }
        catch (ClassNotFoundException e) {
            throw new IllegalStateException("Failed to deserialize object type. ", e);
        }
    }

    public static byte[] serialize(Object object) {
        Assert.notNull(object, "Object must is not null. ");
        ByteArrayOutputStream baos = new ByteArrayOutputStream(INITIAL_SIZE);
        try {
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(object);
            oos.flush();
        }
        catch (IOException e) {
            String msg = "Failed to serialize object of type : ";
            throw new IllegalArgumentException(msg + object.getClass(), e);
        }
        return baos.toByteArray();
    }

    public static Object deserialize(InputStream inputStream) throws IOException {
        Assert.notNull(inputStream, "InputStream must is not null. ");
        ObjectInputStream ois = new ObjectInputStream(inputStream);
        try {
            return ois.readObject();
        }
        catch (ClassNotFoundException e) {
            throw new IllegalStateException("Failed to deserialize object type. ", e);
        }
    }

    public static void serialize(Object object, OutputStream outputStream) throws IOException {
        Assert.notNull(object, "Object must is not null. ");
        Assert.notNull(outputStream, "OutputStream must is not null. ");
        if (!(object instanceof Serializable)) {
            String msg = SerializeUtils.class.getSimpleName();
            msg += " requires a Serializable payload ";
            msg += "but received an object of type [";
            msg += object.getClass().getName() + "]";
            throw new IllegalArgumentException(msg);
        }
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        objectOutputStream.writeObject(object);
        objectOutputStream.flush();
    }

}
