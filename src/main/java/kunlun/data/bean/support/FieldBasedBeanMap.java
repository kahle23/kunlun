/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.data.bean.support;

import kunlun.convert.ConversionService;
import kunlun.data.bean.BeanMap;
import kunlun.exception.ExceptionUtils;
import kunlun.logging.Logger;
import kunlun.logging.LoggerFactory;
import kunlun.reflect.ReflectUtils;
import kunlun.util.Assert;
import kunlun.util.ObjectUtils;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * The field based bean map.
 * @author Kahle
 */
public class FieldBasedBeanMap extends BeanMap {
    private static final Logger log = LoggerFactory.getLogger(FieldBasedBeanMap.class);
    private final Map<String, Field> fieldMap = new LinkedHashMap<String, Field>();
    private Boolean ignoreException = true;
    private Class<?> beanClass;

    public FieldBasedBeanMap() {

    }

    public FieldBasedBeanMap(Object bean) {

        setBean(bean);
    }

    public FieldBasedBeanMap(ConversionService conversionService) {

        setConversionService(conversionService);
    }

    public FieldBasedBeanMap(ConversionService conversionService, Object bean) {
        setConversionService(conversionService);
        setBean(bean);
    }

    public Boolean getIgnoreException() {

        return ignoreException;
    }

    public void setIgnoreException(Boolean ignoreException) {
        Assert.notNull(ignoreException, "Parameter \"ignoreException\" must not null. ");
        this.ignoreException = ignoreException;
    }

    @Override
    public void setBean(Object bean) {
        super.setBean(bean);
        if (beanClass != null && beanClass.equals(bean.getClass())) {
            return;
        }
        beanClass = bean.getClass();
        Field[] declaredFields = ReflectUtils.getDeclaredFields(beanClass);
        for (Field field : declaredFields) {
            if (field != null) {
                ReflectUtils.makeAccessible(field);
                fieldMap.put(field.getName(), field);
            }
        }
    }

    @Override
    protected Object get(Object bean, Object key) {
        Assert.notNull(key, "Parameter \"key\" must not null. ");
        Field field = fieldMap.get(String.valueOf(key));
        if (field == null) { return null; }
        try {
            return field.get(bean);
        }
        catch (Exception e) {
            if (ignoreException) {
                log.debug("Execution \"get\" error. ", e);
                return null;
            }
            else {
                throw ExceptionUtils.wrap(e);
            }
        }
    }

    @Override
    protected Object put(Object bean, Object key, Object value) {
        Assert.notNull(key, "Parameter \"key\" must not null. ");
        Field field = fieldMap.get(String.valueOf(key));
        if (field == null) { return null; }
        Class<?> type = field.getType();
        try {
            if (value == null && type.isPrimitive()) {
                throw new NullPointerException();
            }
            if (getConversionService() != null) {
                value = getConversionService().convert(value, type);
            }
            field.set(bean, value);
            // The always return null.
            // If you want not null, must invoke getter first.
            return null;
        }
        catch (Exception e) {
            if (ignoreException) {
                log.debug("Execution \"put\" error. ", e);
                return null;
            }
            else {
                throw ExceptionUtils.wrap(e);
            }
        }
    }

    @Override
    public Set<Object> keySet() {
        Set<String> keys = fieldMap.keySet();
        return ObjectUtils.cast(Collections.unmodifiableSet(keys));
    }

}
