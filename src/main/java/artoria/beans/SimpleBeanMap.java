package artoria.beans;

import artoria.converter.TypeConverter;
import artoria.exception.ExceptionUtils;
import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.reflect.ReflectUtils;
import artoria.util.ArrayUtils;
import artoria.util.Assert;
import artoria.util.StringUtils;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static artoria.common.Constants.*;

/**
 * Bean map simple implement by jdk.
 * @author Kahle
 */
public class SimpleBeanMap extends BeanMap {
    private static final Integer GET_OR_SET_LENGTH = 3;
    private static Logger log = LoggerFactory.getLogger(SimpleBeanMap.class);
    private Map<String, Method> readMethods = new HashMap<String, Method>();
    private Map<String, Method> writeMethods = new HashMap<String, Method>();
    private Boolean ignoreException = true;
    private Class<?> beanClass;

    public SimpleBeanMap() {
    }

    public SimpleBeanMap(Object bean) {

        this.setBean(bean);
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
        readMethods.putAll(ReflectUtils.findReadMethods(beanClass));
        writeMethods.putAll(ReflectUtils.findWriteMethods(beanClass));
    }

    @Override
    protected Object get(Object bean, Object key) {
        Assert.notNull(key, "Parameter \"key\" must not null. ");
        String keyString = key + EMPTY_STRING;
        if (keyString.startsWith(GET)) {
            keyString = keyString.substring(GET_OR_SET_LENGTH);
            keyString = StringUtils.uncapitalize(keyString);
        }
        Method method = this.readMethods.get(keyString);
        if (method == null) { return null; }
        try {
            return method.invoke(bean);
        }
        catch (Exception e) {
            if (this.ignoreException) {
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
        String keyString = key + EMPTY_STRING;
        if (keyString.startsWith(SET)) {
            keyString = keyString.substring(GET_OR_SET_LENGTH);
            keyString = StringUtils.uncapitalize(keyString);
        }
        Method method = this.writeMethods.get(keyString);
        if (method == null) { return null; }
        Class<?>[] types = method.getParameterTypes();
        TypeConverter cvt = this.getTypeConverter();
        try {
            boolean haveType = ArrayUtils.isNotEmpty(types);
            if (value == null && haveType
                    && types[0].isPrimitive()) {
                throw new NullPointerException();
            }
            if (cvt != null && haveType) {
                value = cvt.convert(value, types[0]);
            }
            // The return always null.
            // If want not null, must invoke getter first.
            return method.invoke(bean, value);
        }
        catch (Exception e) {
            if (this.ignoreException) {
                log.debug("Execution \"put\" error. ", e);
                return null;
            }
            else {
                throw ExceptionUtils.wrap(e);
            }
        }
    }

    @Override
    public Set keySet() {
        Set<String> keys = this.readMethods.keySet();
        return Collections.unmodifiableSet(keys);
    }

}
