package artoria.data.json;

import java.lang.reflect.Type;

/**
 * The json conversion provider based on jdk simple implementation.
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
