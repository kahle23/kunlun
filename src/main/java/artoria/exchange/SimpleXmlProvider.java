package artoria.exchange;

import java.lang.reflect.Type;

/**
 * Xml provider simple implement by jdk.
 * @author Kahle
 */
public class SimpleXmlProvider implements XmlProvider {

    @Override
    public String toXmlString(Object object, XmlFeature... features) {

        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T parseObject(String xmlString, Type type, XmlFeature... features) {

        throw new UnsupportedOperationException();
    }

}
