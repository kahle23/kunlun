package artoria.io.storage;

import artoria.data.KeyValuePair;
import artoria.lang.Storage;
import artoria.util.Assert;
import artoria.util.CollectionUtils;

import java.io.*;
import java.util.Collection;

/**
 * The abstract storage.
 * @author Kahle
 */
public abstract class AbstractStorage implements Storage {

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
    public boolean exist(Object key) {

        return get(key) != null;
    }

    @Override
    public Object list(Object conditions) {

        throw new UnsupportedOperationException("This method is not supported! ");
    }

    public Object put(Object key, Object value) {
        Assert.notNull(value, "Parameter \"value\" must not null. ");
        Assert.notNull(key, "Parameter \"key\" must not null. ");
        return put(new KeyValuePair<Object, Object>(key, value));
    }

    public Object putAll(Collection<?> data) {
        if (CollectionUtils.isEmpty(data)) { return null; }
        for (Object datum : data) { put(datum); }
        return null;
    }

    public Object deleteAll(Collection<?> keys) {
        if (CollectionUtils.isEmpty(keys)) { return null; }
        for (Object key : keys) { delete(key); }
        return null;
    }

}
