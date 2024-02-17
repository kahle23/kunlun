/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.data.serialize.support;

import kunlun.core.Serializer;
import kunlun.exception.ExceptionUtils;
import kunlun.util.Assert;

import java.io.*;

/**
 * The serializer simple implement by jdk.
 * @author Kahle
 */
public class SimpleSerializer implements Serializer {

    @Override
    public byte[] serialize(Object object) {
        Assert.notNull(object, "Parameter \"object\" must not null. ");
        Assert.isInstanceOf(Serializable.class, object, SimpleSerializer.class.getSimpleName()
                + " requires a Serializable payload but received an object of type ["
                + object.getClass().getName() + "]. ");
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(object);
            objectOutputStream.flush();
            return outputStream.toByteArray();
        }
        catch (Exception e) {
            throw ExceptionUtils.wrap(e);
        }
    }

    @Override
    public Object deserialize(Object data) {
        Assert.notNull(data, "Parameter \"data\" must not null. ");
        Assert.isInstanceOf(byte[].class, data
                , "Parameter \"data\" must is instance of byte[]. ");
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream((byte[]) data);
            ObjectInputStream ois = new ObjectInputStream(inputStream);
            return ois.readObject();
        }
        catch (Exception e) {
            throw ExceptionUtils.wrap(e);
        }
    }

}
