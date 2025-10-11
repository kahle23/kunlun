/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.data.json;

import kunlun.data.json.support.SimpleJsonProcessor;
import kunlun.logging.Logger;
import kunlun.logging.LoggerFactory;
import kunlun.util.Assert;

import java.lang.reflect.Type;

/**
 * The json conversion tools.
 * @author Kahle
 */
public class JsonUtil {
    private static final Logger log = LoggerFactory.getLogger(JsonUtil.class);
    private static volatile JsonProvider jsonProvider;

    public static JsonProvider getJsonProvider() {
        if (jsonProvider != null) { return jsonProvider; }
        synchronized (JsonUtil.class) {
            if (jsonProvider != null) { return jsonProvider; }
            JsonUtil.setJsonProvider(new SimpleJsonProvider());
            // Register the default processor.
            registerProcessor(getDefaultProcessorName(), new SimpleJsonProcessor());
            return jsonProvider;
        }
    }

    public static void setJsonProvider(JsonProvider jsonProvider) {
        Assert.notNull(jsonProvider, "Parameter \"jsonProvider\" must not null. ");
        log.debug("Set json provider: {}", jsonProvider.getClass().getName());
        JsonUtil.jsonProvider = jsonProvider;
    }

    public static String getDefaultProcessorName() {

        return getJsonProvider().getDefaultProcessorName();
    }

    public static void setDefaultProcessorName(String defaultProcessorName) {

        getJsonProvider().setDefaultProcessorName(defaultProcessorName);
    }

    public static void registerProcessor(String name, JsonProcessor jsonProcessor) {

        getJsonProvider().registerProcessor(name, jsonProcessor);
    }

    public static void deregisterProcessor(String name) {

        getJsonProvider().deregisterProcessor(name);
    }

    public static JsonProcessor getJsonProcessor(String name) {

        return getJsonProvider().getJsonProcessor(name);
    }

    public static boolean isJsonObject(String jsonString) {

        return getJsonProvider().isJsonObject(getDefaultProcessorName(), jsonString);
    }

    public static boolean isJsonObject(String name, String jsonString) {

        return getJsonProvider().isJsonObject(name, jsonString);
    }

    public static boolean isJsonArray(String jsonString) {

        return getJsonProvider().isJsonArray(getDefaultProcessorName(), jsonString);
    }

    public static boolean isJsonArray(String name, String jsonString) {

        return getJsonProvider().isJsonArray(name, jsonString);
    }

    public static String toJsonString(Object object, Object... arguments) {

        return getJsonProvider().toJsonString(getDefaultProcessorName(), object, arguments);
    }

    public static String toJsonString(String name, Object object, Object... arguments) {

        return getJsonProvider().toJsonString(name, object, arguments);
    }

    public static <T> T parseObject(String jsonString, Type type, Object... arguments) {

        return getJsonProvider().parseObject(getDefaultProcessorName(), jsonString, type, arguments);
    }

    public static <T> T parseObject(String name, String jsonString, Type type, Object... arguments) {

        return getJsonProvider().parseObject(name, jsonString, type, arguments);
    }

}
