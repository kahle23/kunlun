package com.apyhs.artoria.serialize;

import com.apyhs.artoria.exception.UncheckedException;
import com.apyhs.artoria.util.Assert;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

/**
 * JDK deserializer.
 * @author Kahle
 */
public class DefaultDeserializer implements Deserializer<Object> {

    @Override
    public Object deserialize(InputStream inputStream) throws IOException {
        try {
            Assert.notNull(inputStream, "InputStream must is not null. ");
            ObjectInputStream ois = new ObjectInputStream(inputStream);
            return ois.readObject();
        }
        catch (ClassNotFoundException e) {
            throw new UncheckedException(e);
        }
    }

}
