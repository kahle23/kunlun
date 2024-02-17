/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.io.storage;

import kunlun.data.tuple.KeyValueImpl;
import kunlun.io.DataStorage;
import kunlun.util.Assert;
import kunlun.util.CollectionUtils;

import java.io.*;
import java.util.Collection;

import static kunlun.common.constant.Numbers.*;

/**
 * The abstract data storage.
 * @author Kahle
 */
public abstract class AbstractDataStorage implements DataStorage {

    protected InputStream convertToStream(Object value, String charset) throws IOException {
        if (value instanceof File) {
            return new FileInputStream((File) value);
        }
        else if (value instanceof InputStream) {
            return (InputStream) value;
        }
        else if (value instanceof byte[]) {
            return new ByteArrayInputStream((byte[]) value);
        }
        else if (value instanceof String) {
            byte[] bytes = ((String) value).getBytes(charset);
            return new ByteArrayInputStream(bytes);
        }
        else {
            throw new IllegalArgumentException("Parameter \"value\" is not supported. ");
        }
    }

    @Override
    public Object execute(Object[] arguments) {
        String methodName = String.valueOf(arguments[ZERO]);
        Object input = arguments[ONE];
        Object type = arguments[TWO];
        if ("exist".equals(methodName)) { return exist(input); }
        if ("get".equals(methodName)) { return get(input); }
        if ("put".equals(methodName)) { return put(input); }
        if ("putAll".equals(methodName)) { return putAll((Collection<?>) input); }
        if ("delete".equals(methodName)) { return delete(input); }
        if ("deleteAll".equals(methodName)) { return deleteAll((Collection<?>) input); }
        if ("list".equals(methodName)) { return list(input); }
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean exist(Object key) {

        return get(key) != null;
    }

    @Override
    public Object list(Object conditions) {

        throw new UnsupportedOperationException("This method is not supported! ");
    }

    @Override
    public Object put(Object key, Object value) {
        Assert.notNull(value, "Parameter \"value\" must not null. ");
        Assert.notNull(key, "Parameter \"key\" must not null. ");
        return put(new KeyValueImpl<Object, Object>(key, value));
    }

    @Override
    public Object putAll(Collection<?> data) {
        if (CollectionUtils.isEmpty(data)) { return null; }
        for (Object datum : data) { put(datum); }
        return null;
    }

    @Override
    public Object deleteAll(Collection<?> keys) {
        if (CollectionUtils.isEmpty(keys)) { return null; }
        for (Object key : keys) { delete(key); }
        return null;
    }

}
