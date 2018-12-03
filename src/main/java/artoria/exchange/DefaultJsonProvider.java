package artoria.exchange;

import java.lang.reflect.Type;

/**
 * Json provider simple implement by jdk.
 * @author Kahle
 */
public class DefaultJsonProvider implements JsonProvider {
    private boolean prettyFormat;

    public DefaultJsonProvider() {

        this(false);
    }

    public DefaultJsonProvider(boolean prettyFormat) {

        this.prettyFormat = prettyFormat;
    }

    @Override
    public boolean getPrettyFormat() {

        return this.prettyFormat;
    }

    @Override
    public String toJsonString(Object object) {

        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T parseObject(String text, Type type) {

        throw new UnsupportedOperationException();
    }

}
