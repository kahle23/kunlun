/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.data.xml;

import kunlun.data.xml.support.SimpleXmlProcessor;
import kunlun.logging.Logger;
import kunlun.logging.LoggerFactory;
import kunlun.util.Assert;

import java.lang.reflect.Type;

/**
 * The xml conversion tools.
 * @author Kahle
 */
public class XmlUtil {
    private static final Logger log = LoggerFactory.getLogger(XmlUtil.class);
    private static volatile XmlProvider xmlProvider;

    public static XmlProvider getXmlProvider() {
        if (xmlProvider != null) { return xmlProvider; }
        synchronized (XmlUtil.class) {
            if (xmlProvider != null) { return xmlProvider; }
            XmlUtil.setXmlProvider(new SimpleXmlProvider());
            // Register the default handler.
            registerProcessor(getDefaultProcessorName(), new SimpleXmlProcessor());
            return xmlProvider;
        }
    }

    public static void setXmlProvider(XmlProvider xmlProvider) {
        Assert.notNull(xmlProvider, "Parameter \"xmlProvider\" must not null. ");
        log.debug("Set xml provider: {}", xmlProvider.getClass().getName());
        XmlUtil.xmlProvider = xmlProvider;
    }

    public static String getDefaultProcessorName() {

        return getXmlProvider().getDefaultProcessorName();
    }

    public static void setDefaultProcessorName(String defaultProcessorName) {

        getXmlProvider().setDefaultProcessorName(defaultProcessorName);
    }

    public static void registerProcessor(String name, XmlProcessor xmlHandler) {

        getXmlProvider().registerProcessor(name, xmlHandler);
    }

    public static void deregisterProcessor(String name) {

        getXmlProvider().deregisterProcessor(name);
    }

    public static XmlProcessor getXmlProcessor(String name) {

        return getXmlProvider().getXmlProcessor(name);
    }

    public static String toXmlString(Object object, Object... arguments) {

        return getXmlProvider().toXmlString(getDefaultProcessorName(), object, arguments);
    }

    public static String toXmlString(String name, Object object, Object... arguments) {

        return getXmlProvider().toXmlString(name, object, arguments);
    }

    public static <T> T parseObject(String xmlString, Type type, Object... arguments) {

        return getXmlProvider().parseObject(getDefaultProcessorName(), xmlString, type, arguments);
    }

    public static <T> T parseObject(String name, String xmlString, Type type, Object... arguments) {

        return getXmlProvider().parseObject(name, xmlString, type, arguments);
    }

}
