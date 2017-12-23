package apyh.artoria.serialize;

import apyh.artoria.util.Assert;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

/**
 * JDK deserializer
 * @author Kahle
 */
public class JavaDeserializer implements Deserializer {

    @Override
    public Object deserialize(InputStream inputStream) throws IOException, ClassNotFoundException {
        Assert.notNull(inputStream, "InputStream must is not null. ");
        ObjectInputStream ois = new ObjectInputStream(inputStream);
        return ois.readObject();
    }

}
