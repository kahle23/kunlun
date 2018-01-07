package com.apyhs.artoria.beans;

import com.apyhs.artoria.converter.Converter;
import com.apyhs.artoria.exception.UncheckedException;
import com.apyhs.artoria.util.ArrayUtils;
import com.apyhs.artoria.util.Assert;
import com.apyhs.artoria.reflect.ReflectUtils;
import com.apyhs.artoria.util.StringUtils;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static com.apyhs.artoria.util.StringConstant.STRING_GET;
import static com.apyhs.artoria.util.StringConstant.STRING_SET;

/**
 * Jdk bean map.
 * @author Kahle
 */
public class JdkBeanMap extends BeanMap {
    private static final Integer GET_OR_SET_LENGTH = 3;

    private HashMap<Object, Method> getters = new HashMap<Object, Method>();
    private HashMap<Object, Method> setters = new HashMap<Object, Method>();
    private Class<?> beanClass;

    public JdkBeanMap() {}

    public JdkBeanMap(Object bean) {
        this.setBean(bean);
    }

    @Override
    public void setBean(Object bean) {
        Assert.notNull(bean, "Parameter \"bean\" must not null. ");
        super.setBean(bean);
        if (this.beanClass != null && this.beanClass == bean.getClass()) {
            return;
        }
        this.beanClass = bean.getClass();
        Map<String, Method> map = ReflectUtils.findAllGetterAndSetter(bean.getClass());
        for (Entry<String, Method> entry : map.entrySet()) {
            String key = entry.getKey();
            String name = key.substring(GET_OR_SET_LENGTH);
            name = StringUtils.uncapitalize(name);
            Method method = entry.getValue();
            if (key.startsWith(STRING_GET)) {
                getters.put(name, method);
            }
            else if (key.startsWith(STRING_SET)) {
                setters.put(name, method);
            }
        }
    }

    @Override
    protected Object get(Object bean, Object key) {
        Method method = getters.get(key);
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
        Method method = setters.get(key);
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
        Set<Object> keys = getters.keySet();
        return Collections.unmodifiableSet(keys);
    }

}
