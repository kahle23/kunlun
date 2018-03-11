package com.github.kahlkn.artoria.beans;

import com.github.kahlkn.artoria.converter.Converter;
import com.github.kahlkn.artoria.exception.UncheckedException;
import com.github.kahlkn.artoria.reflect.ReflectUtils;
import com.github.kahlkn.artoria.util.ArrayUtils;
import com.github.kahlkn.artoria.util.Assert;
import com.github.kahlkn.artoria.util.StringUtils;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static com.github.kahlkn.artoria.util.Const.*;

/**
 * Jdk bean map.
 * @author Kahle
 */
public class JdkBeanMap extends BeanMap {

    private HashMap<Object, Method> readMethods = new HashMap<Object, Method>();
    private HashMap<Object, Method> writeMethods = new HashMap<Object, Method>();
    private Class<?> beanClass;

    public JdkBeanMap() {}

    public JdkBeanMap(Object bean) {
        this.setBean(bean);
    }

    @Override
    public void setBean(Object bean) {
        super.setBean(bean);
        if (this.beanClass != null && this.beanClass == bean.getClass()) {
            return;
        }
        this.beanClass = bean.getClass();
        Map<String, Method> readMethods = ReflectUtils.findReadMethods(this.beanClass);
        for (Entry<String, Method> entry : readMethods.entrySet()) {
            String key = entry.getKey();
            if (key.startsWith(GET)) {
                key = key.substring(GET_OR_SET_LENGTH);
                key = StringUtils.uncapitalize(key);
            }
            this.readMethods.put(key, entry.getValue());
        }
        Map<String, Method> writeMethods = ReflectUtils.findWriteMethods(this.beanClass);
        for (Entry<String, Method> entry : writeMethods.entrySet()) {
            String key = entry.getKey();
            if (key.startsWith(SET)) {
                key = key.substring(GET_OR_SET_LENGTH);
                key = StringUtils.uncapitalize(key);
            }
            this.writeMethods.put(key, entry.getValue());
        }
    }

    @Override
    protected Object get(Object bean, Object key) {
        Assert.notNull(key, "Parameter \"key\" must not null. ");
        String keyString = key + EMPTY_STRING;
        if (keyString.startsWith(GET)) {
            keyString = keyString.substring(GET_OR_SET_LENGTH);
            keyString = StringUtils.uncapitalize(keyString);
        }
        Method method = readMethods.get(keyString);
        if (method == null) { return null; }
        try {
            return method.invoke(bean);
        }
        catch (Exception e) {
            throw new UncheckedException(e);
        }
    }

    @Override
    protected Object put(Object bean, Object key, Object value) {
        Assert.notNull(key, "Parameter \"key\" must not null. ");
        String keyString = key + EMPTY_STRING;
        if (keyString.startsWith(SET)) {
            keyString = keyString.substring(GET_OR_SET_LENGTH);
            keyString = StringUtils.uncapitalize(keyString);
        }
        Method method = writeMethods.get(keyString);
        if (method == null) { return null; }
        Converter cvt = this.getConverter();
        Class<?>[] types = method.getParameterTypes();
        if (cvt != null && ArrayUtils.isNotEmpty(types)) {
            value = cvt.convert(value, types[0]);
        }
        try {
            // The return always null.
            // If want not null, must invoke getter first.
            return method.invoke(bean, value);
        }
        catch (Exception e) {
            throw new UncheckedException(e);
        }
    }

    @Override
    public Set keySet() {
        Set<Object> keys = readMethods.keySet();
        return Collections.unmodifiableSet(keys);
    }

}
