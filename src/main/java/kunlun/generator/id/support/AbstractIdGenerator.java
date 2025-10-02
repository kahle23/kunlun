/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.generator.id.support;

import kunlun.generator.id.IdGenerator;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import static kunlun.util.Assert.notNull;
import static kunlun.util.Assert.renderMessage;

/**
 * 抽象的 ID 生成器（定义了一些常用方法）.
 * @author Kahle
 */
public abstract class AbstractIdGenerator implements IdGenerator {
    protected static final String TIME_STRING_KEY = "time-string";
    protected static final String PREFIX_KEY = "prefix";
    protected static final String SUFFIX_KEY = "suffix";
    private Map<Object, Object> commonProperties = Collections.emptyMap();

    @Override
    public Map<Object, Object> getCommonProperties() {

        return commonProperties;
    }

    @Override
    public void setCommonProperties(Map<?, ?> properties) {
        notNull(properties, "Parameter \"properties\" must not null. ");
        this.commonProperties = Collections.unmodifiableMap(properties);
    }

    /**
     * 获取 ID 生成器的生成规则的描述.
     * @return 获取到的描述
     */
    public String getDescription() {

        return null;
    }

    /**
     * 获取（生成）时间字符串.
     * @param context 上下文对象
     * @return 获取到的时间字符串
     */
    protected String obtainTimeString(Context context) {
        Object strObJ = context.getStorage().get(TIME_STRING_KEY);
        return strObJ != null ? String.valueOf(strObJ) : null;
    }

    /**
     * 获取（生成）前缀.
     * @param context 上下文对象
     * @return 获取到的前缀
     */
    protected String obtainPrefix(Context context) {
        Object strObJ = context.getStorage().get(PREFIX_KEY);
        return strObJ != null ? String.valueOf(strObJ) : null;
    }

    /**
     * 获取（生成）后缀.
     * @param context 上下文对象
     * @return 获取到的后缀
     */
    protected String obtainSuffix(Context context) {
        Object strObJ = context.getStorage().get(SUFFIX_KEY);
        return strObJ != null ? String.valueOf(strObJ) : null;
    }

    /**
     * 基于上下文对象的较为通用的 ID 生成逻辑
     * @param context 上下文对象
     * @return 生成的 ID
     */
    protected Object generateId(Context context) {
        throw new UnsupportedOperationException(renderMessage(
                "In \"%s\", the method \"generateId\" is not supported! ", getClass().getName()
        ));
    }

    /**
     * ID 生成器的上下文实现类.
     * @author Kahle
     */
    public static class ContextImpl implements Context {
        private final Map<String, Object> storage;
        private final Object[] arguments;
        private final Object config;

        public ContextImpl(Map<String, Object> storage, Object config, Object[] arguments) {
            this.arguments = notNull(arguments);
            this.storage = notNull(storage);
            this.config = config;
        }

        public ContextImpl(Object config, Object[] arguments) {

            this(new LinkedHashMap<String, Object>(), config, arguments);
        }

        @Override
        public Map<String, Object> getStorage() {

            return storage;
        }

        @Override
        public Object[] getArguments() {

            return arguments;
        }

        @Override
        public Object getConfig() {

            return config;
        }
    }

}
