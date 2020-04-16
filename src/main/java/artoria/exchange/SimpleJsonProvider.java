package artoria.exchange;

import java.lang.reflect.Type;

/**
 * Json provider simple implement by jdk.
 * @author Kahle
 */
public class SimpleJsonProvider implements JsonProvider {
    private boolean prettyFormat;

    public SimpleJsonProvider() {

        this(false);
    }

    public SimpleJsonProvider(boolean prettyFormat) {

        this.prettyFormat = prettyFormat;
    }

    @Override
    public boolean getPrettyFormat() {

        return prettyFormat;
    }

    @Override
    public String toJsonString(Object object) {

        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T parseObject(String jsonString, Type type) {

        throw new UnsupportedOperationException();
    }

}
