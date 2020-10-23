package artoria.exchange;

import java.lang.reflect.Type;

/**
 * Json provider simple implement by jdk.
 * @author Kahle
 */
public class SimpleJsonProvider implements JsonProvider {

    @Override
    public String toJsonString(Object object, JsonFeature... features) {

        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T parseObject(String jsonString, Type type, JsonFeature... features) {

        throw new UnsupportedOperationException();
    }

}
