package artoria.exchange;

import java.util.List;

/**
 * Json handler simple implement by jdk.
 * @author Kahle
 */
public class SimpleJsonHandler implements JsonHandler {

    @Override
    public <T> T parseObject(String text, Class<T> clazz) {

        throw new UnsupportedOperationException();
    }

    @Override
    public <T> List<T> parseArray(String text, Class<T> clazz) {

        throw new UnsupportedOperationException();
    }

    @Override
    public String toJsonString(Object object) {

        return this.toJsonString(object, false);
    }

    @Override
    public String toJsonString(Object object, boolean prettyFormat) {

        throw new UnsupportedOperationException();
    }

}
