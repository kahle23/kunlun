package artoria.exchange;

import artoria.data.xml.XmlProvider;

import java.lang.reflect.Type;

/**
 * Xml tools.
 * @author Kahle
 */
@Deprecated // TODO: 2023/3/7 Deletable
public class XmlUtils {

    public static XmlProvider getXmlProvider() {

        return artoria.data.xml.XmlUtils.getXmlProvider();
    }

    public static void setXmlProvider(XmlProvider xmlProvider) {

        artoria.data.xml.XmlUtils.setXmlProvider(xmlProvider);
    }

    public static String toXmlString(Object object, Object... arguments) {

        return artoria.data.xml.XmlUtils.toXmlString(object, arguments);
    }

    public static <T> T parseObject(String xmlString, Type type, Object... arguments) {

        return artoria.data.xml.XmlUtils.parseObject(xmlString, type, arguments);
    }

}
