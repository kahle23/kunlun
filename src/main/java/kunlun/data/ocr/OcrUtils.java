/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.data.ocr;

import kunlun.logging.Logger;
import kunlun.logging.LoggerFactory;
import kunlun.util.Assert;

/**
 * The OCR tools.
 * @author Kahle
 */
public class OcrUtils {
    private static final Logger log = LoggerFactory.getLogger(OcrUtils.class);
    private static volatile OcrProvider ocrProvider;
    private static String defaultHandlerName = "default";

    public static OcrProvider getOcrProvider() {
        if (ocrProvider != null) { return ocrProvider; }
        synchronized (OcrUtils.class) {
            if (ocrProvider != null) { return ocrProvider; }
            OcrUtils.setOcrProvider(new SimpleOcrProvider());
            return ocrProvider;
        }
    }

    public static void setOcrProvider(OcrProvider ocrProvider) {
        Assert.notNull(ocrProvider, "Parameter \"ocrProvider\" must not null. ");
        log.info("Set ocr provider: {}", ocrProvider.getClass().getName());
        OcrUtils.ocrProvider = ocrProvider;
    }

    public static String getDefaultHandlerName() {

        return defaultHandlerName;
    }

    public static void setDefaultHandlerName(String defaultHandlerName) {
        Assert.notBlank(defaultHandlerName, "Parameter \"defaultHandlerName\" must not blank. ");
        OcrUtils.defaultHandlerName = defaultHandlerName;
    }

    public static void registerHandler(String name, OcrHandler ocrHandler) {

        getOcrProvider().registerHandler(name, ocrHandler);
    }

    public static void deregisterHandler(String name) {

        getOcrProvider().deregisterHandler(name);
    }

    public static OcrHandler getOcrHandler(String name) {

        return getOcrProvider().getOcrHandler(name);
    }

    public static <T> T handle(Object type, Object data, Class<T> clazz, Object... arguments) {

        return getOcrProvider().handle(getDefaultHandlerName(), type, data, clazz, arguments);
    }

    public static <T> T handle(String name, Object type, Object data, Class<T> clazz, Object... arguments) {

        return getOcrProvider().handle(name, type, data, clazz, arguments);
    }

}
