package artoria.data.xml;

import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.util.Assert;

import java.lang.reflect.Type;

/**
 * The xml conversion tools.
 * @author Kahle
 */
public class XmlUtils {
    private static final Logger log = LoggerFactory.getLogger(XmlUtils.class);
    private static volatile XmlProvider xmlProvider;

    public static XmlProvider getXmlProvider() {
        if (xmlProvider != null) { return xmlProvider; }
        synchronized (XmlUtils.class) {
            if (xmlProvider != null) { return xmlProvider; }
            XmlUtils.setXmlProvider(new SimpleXmlProvider());
            return xmlProvider;
        }
    }

    public static void setXmlProvider(XmlProvider xmlProvider) {
        Assert.notNull(xmlProvider, "Parameter \"xmlProvider\" must not null. ");
        log.info("Set xml provider: {}", xmlProvider.getClass().getName());
        XmlUtils.xmlProvider = xmlProvider;
    }

    public static String toXmlString(Object object, Object... arguments) {

        return getXmlProvider().toXmlString(object, arguments);
    }

    public static <T> T parseObject(String xmlString, Type type, Object... arguments) {

        return getXmlProvider().parseObject(xmlString, type, arguments);
    }

}
