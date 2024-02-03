package artoria.data.xml.support;

import java.lang.reflect.Type;

/**
 * The xml conversion handler based on jdk simple implementation.
 * @author Kahle
 */
public class SimpleXmlHandler extends AbstractXmlHandler {

    @Override
    public String toXmlString(Object object, Object... arguments) {

        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T parseObject(String jsonString, Type type, Object... arguments) {

        throw new UnsupportedOperationException();
    }

}
