package com.apyhs.artoria.serialize;

import com.apyhs.artoria.util.Assert;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;

/**
 * JDK serializer.
 * @author Kahle
 */
public class DefaultSerializer implements Serializer<Object> {

    @Override
    public void serialize(Object object, OutputStream outputStream) throws IOException {
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
