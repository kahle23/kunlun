package artoria.storage.support;

import artoria.io.IOUtils;
import artoria.storage.AbstractStorage;
import artoria.util.StringUtils;

import java.io.*;

import static artoria.common.Constants.UTF_8;
import static artoria.util.ObjectUtils.cast;

/**
 * The abstract IO stream storage.
 * @author Kahle
 */
public abstract class AbstractStreamStorage extends AbstractStorage {
    private final String charset;

    public AbstractStreamStorage(String name, String charset) {
        super(name);
        if (StringUtils.isBlank(charset)) { charset = UTF_8; }
        this.charset = charset;
    }

    public String getCharset() {

        return charset;
    }

    protected <T> T convertToResult(InputStream inputStream, String charset, Class<T> type) throws IOException {
        if (byte[].class.isAssignableFrom(type)) {
            return cast(IOUtils.toByteArray(inputStream));
        }
        else if (String.class.isAssignableFrom(type)) {
            byte[] bytes = IOUtils.toByteArray(inputStream);
            return cast(new String(bytes, charset));
        }
        else {
            throw new IllegalArgumentException("Parameter \"type\" is not supported. ");
        }
    }

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
    public Object get(Object key) {

        throw new UnsupportedOperationException();
    }

}
