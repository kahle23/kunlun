package artoria.data.bean;

import artoria.convert.ConversionService;
import artoria.exception.ExceptionUtils;
import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.reflect.ReflectUtils;
import artoria.util.ArrayUtils;
import artoria.util.Assert;
import artoria.util.ObjectUtils;
import artoria.util.StringUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import static artoria.common.Constants.*;

/**
 * The bean map simple implement by jdk.
 * @author Kahle
 */
public class SimpleBeanMap extends BeanMap {
    private static final Logger log = LoggerFactory.getLogger(SimpleBeanMap.class);
    private static final Method METHOD_GET_CLASS;
    private final Map<String, Method> writeMethodMap = new LinkedHashMap<String, Method>();
    private final Map<String, Method> readMethodMap = new LinkedHashMap<String, Method>();
    private Boolean ignoreException = true;
    private Class<?> beanClass;

    static {
        try {
            String nameGetClass = "getClass";
            METHOD_GET_CLASS = ReflectUtils.getMethod(Object.class, nameGetClass);
        }
        catch (NoSuchMethodException e) {
            throw ExceptionUtils.wrap(e);
        }
    }

    public SimpleBeanMap() {
    }

    public SimpleBeanMap(Object bean) {

        setBean(bean);
    }

    public SimpleBeanMap(ConversionService conversionService) {

        setConversionService(conversionService);
    }

    public SimpleBeanMap(ConversionService conversionService, Object bean) {
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
        PropertyDescriptor[] descriptors = ReflectUtils.getPropertyDescriptors(beanClass);
        for (PropertyDescriptor descriptor : descriptors) {
            Method writeMethod = descriptor.getWriteMethod();
            Method readMethod = descriptor.getReadMethod();
            String name = descriptor.getName();
            if (METHOD_GET_CLASS.equals(readMethod)) { continue; }
            if (writeMethod != null) { writeMethodMap.put(name, writeMethod); }
            if (readMethod != null) { readMethodMap.put(name, readMethod); }
        }
    }

    @Override
    protected Object get(Object bean, Object key) {
        Assert.notNull(key, "Parameter \"key\" must not null. ");
        String keyString = String.valueOf(key);
        if (keyString.startsWith(GET)) {
            keyString = keyString.substring(THREE);
            keyString = StringUtils.uncapitalize(keyString);
        }
        Method method = readMethodMap.get(keyString);
        if (method == null) { return null; }
        try {
            return method.invoke(bean);
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
        String keyString = String.valueOf(key);
        if (keyString.startsWith(SET)) {
            keyString = keyString.substring(THREE);
            keyString = StringUtils.uncapitalize(keyString);
        }
        Method method = writeMethodMap.get(keyString);
        if (method == null) { return null; }
        Class<?>[] types = method.getParameterTypes();
        try {
            boolean haveType = ArrayUtils.isNotEmpty(types);
            if (value == null && haveType
                    && types[ZERO].isPrimitive()) {
                throw new NullPointerException();
            }
            if (getConversionService() != null && haveType) {
                value = getConversionService().convert(value, types[ZERO]);
            }
            // The always return null.
            // If you want not null, must invoke getter first.
            return method.invoke(bean, value);
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
        Set<String> keys = readMethodMap.keySet();
        return ObjectUtils.cast(Collections.unmodifiableSet(keys));
    }

}
