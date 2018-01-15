package com.apyhs.artoria.beans;

import com.apyhs.artoria.converter.Converter;
import com.apyhs.artoria.exception.UncheckedException;
import com.apyhs.artoria.reflect.ReflectUtils;
import com.apyhs.artoria.util.ArrayUtils;
import com.apyhs.artoria.util.Assert;
import com.apyhs.artoria.util.StringUtils;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static com.apyhs.artoria.util.Const.*;

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
        this.readMethods.putAll(readMethods);
        Map<String, Method> writeMethods = ReflectUtils.findWriteMethods(this.beanClass);
        this.writeMethods.putAll(writeMethods);
    }

    @Override
    protected Object get(Object bean, Object key) {
        Assert.notNull(key, "Parameter \"key\" must not null. ");
        String methodName = key + EMPTY_STRING;
        if (!methodName.startsWith(GET)) {
            methodName = GET + StringUtils.capitalize(methodName);
        }
        Method method = readMethods.get(methodName);
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
        String methodName = key + EMPTY_STRING;
        if (!methodName.startsWith(SET)) {
            methodName = SET + StringUtils.capitalize(methodName);
        }
        Method method = writeMethods.get(methodName);
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
