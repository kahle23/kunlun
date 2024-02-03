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

    public static String getDefaultHandlerName() {

        return getXmlProvider().getDefaultHandlerName();
    }

    public static void setDefaultHandlerName(String defaultHandlerName) {

        getXmlProvider().setDefaultHandlerName(defaultHandlerName);
    }

    public static void registerHandler(String name, XmlHandler xmlHandler) {

        getXmlProvider().registerHandler(name, xmlHandler);
    }

    public static void deregisterHandler(String name) {

        getXmlProvider().deregisterHandler(name);
    }

    public static XmlHandler getXmlHandler(String name) {

        return getXmlProvider().getXmlHandler(name);
    }

    public static String toXmlString(Object object, Object... arguments) {

        return getXmlProvider().toXmlString(getDefaultHandlerName(), object, arguments);
    }

    public static String toXmlString(String name, Object object, Object... arguments) {

        return getXmlProvider().toXmlString(name, object, arguments);
    }

    public static <T> T parseObject(String xmlString, Type type, Object... arguments) {

        return getXmlProvider().parseObject(getDefaultHandlerName(), xmlString, type, arguments);
    }

    public static <T> T parseObject(String name, String xmlString, Type type, Object... arguments) {

        return getXmlProvider().parseObject(name, xmlString, type, arguments);
    }

}
