package artoria.data.xml;

import java.lang.reflect.Type;

/**
 * The xml conversion provider based on jdk simple implementation.
 * @author Kahle
 */
public class SimpleXmlProvider implements XmlProvider {

    @Override
    public String toXmlString(Object object, Object... arguments) {

        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T parseObject(String xmlString, Type type, Object... arguments) {

        throw new UnsupportedOperationException();
    }

}
