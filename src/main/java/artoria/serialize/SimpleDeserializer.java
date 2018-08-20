package artoria.serialize;

import artoria.exception.ExceptionUtils;
import artoria.util.Assert;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

/**
 * Deserializer simple implement by jdk.
 * @author Kahle
 */
public class SimpleDeserializer implements Deserializer<Object> {

    @Override
    public Object deserialize(InputStream inputStream) throws IOException {
        Assert.notNull(inputStream, "Parameter \"inputStream\" must not null. ");
        try {
            ObjectInputStream ois = new ObjectInputStream(inputStream);
            return ois.readObject();
        }
        catch (ClassNotFoundException e) {
            throw ExceptionUtils.wrap(e);
        }
    }

}
