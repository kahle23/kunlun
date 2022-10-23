package artoria.data.bean;

import artoria.convert.ConversionProvider;
import artoria.util.Assert;
import artoria.util.ObjectUtils;

import java.util.*;

import static artoria.common.Constants.*;

/**
 * The bean map.
 * @author Kahle
 */
public abstract class BeanMap implements Map<Object, Object>, Cloneable {
    private ConversionProvider conversionProvider;
    private Object bean;

    public ConversionProvider getConversionProvider() {

        return conversionProvider;
    }

    public void setConversionProvider(ConversionProvider conversionProvider) {

        this.conversionProvider = conversionProvider;
    }

    public Object getBean() {

        return bean;
    }

    public void setBean(Object bean) {
        Assert.notNull(bean, "Parameter \"bean\" must not null. ");
        this.bean = bean;
    }

    /**
     * Get the value who property is key's value from bean.
     * @param bean The bean map's bean
     * @param key The bean's property
     * @return The bean's value
     */
    protected abstract Object get(Object bean, Object key);

    /**
     * Put the value who property is key's value from bean.
     * @param bean The bean map's bean
     * @param key The bean's property
     * @param value The bean's value want to put
     * @return The old value about the property
     */
    protected abstract Object put(Object bean, Object key, Object value);

    @Override
    public int size() {

        return keySet().size();
    }

    @Override
    public boolean isEmpty() {

        return size() == ZERO;
    }

    @Override
    public boolean containsKey(Object key) {

        return keySet().contains(key);
    }

    @Override
    public boolean containsValue(Object value) {
        Set<Object> keys = keySet();
        for (Object key : keys) {
            Object thisVal = get(key);
            boolean isEqual = (
                    value == null && thisVal == null
            ) || (
                    value != null && value.equals(thisVal)
            );
            if (isEqual) { return true; }
        }
        return false;
    }

    @Override
    public Object get(Object key) {

        return get(bean, key);
    }

    @Override
    public Object put(Object key, Object value) {

        return put(bean, key, value);
    }

    @Override
    public Object remove(Object key) {

        throw new UnsupportedOperationException();
    }

    @Override
    public void putAll(Map<?, ?> map) {
        Set<Object> keys = keySet();
        for (Object key : keys) {
            put(key, map.get(key));
        }
    }

    @Override
    public void clear() {

        throw new UnsupportedOperationException();
    }

    @Override
    public Collection<Object> values() {
        Set<Object> keys = keySet();
        List<Object> values = new ArrayList<Object>(keys.size());
        for (Object key : keys) {
            values.add(get(key));
        }
        return Collections.unmodifiableCollection(values);
    }

    @Override
    public Set<Entry<Object, Object>> entrySet() {
        Set<Object> keys = keySet();
        int size = keys.size();
        Map<Object, Object> newMap = new HashMap<Object, Object>(size);
        for (Object key : keys) {
            newMap.put(key, get(key));
        }
        return Collections.unmodifiableMap(newMap).entrySet();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Map)) {
            return false;
        }
        Map other = (Map) obj;
        if (size() != other.size()) {
            return false;
        }
        Set keys = keySet();
        for (Object key : keys) {
            if (!other.containsKey(key)) {
                return false;
            }
            Object thisVal = get(key);
            Object otherVal = other.get(key);
            if (!ObjectUtils.equals(thisVal, otherVal)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        int code = ZERO;
        Set keys = keySet();
        for (Object key : keys) {
            Object val = get(key);
            int keyCode = key == null ? ZERO : key.hashCode();
            int valCode = val == null ? ZERO : val.hashCode();
            code += keyCode ^ valCode;
        }
        return code;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(LEFT_CURLY_BRACKET);
        Set keys = keySet();
        for (Object key : keys) {
            builder.append(key);
            builder.append(EQUAL);
            builder.append(get(key));
            builder.append(COMMA);
            builder.append(BLANK_SPACE);
        }
        if (!keys.isEmpty()) {
            builder.deleteCharAt(builder.length() - ONE);
            builder.deleteCharAt(builder.length() - ONE);
        }
        builder.append(RIGHT_CURLY_BRACKET);
        return builder.toString();
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        BeanMap newMap = (BeanMap) super.clone();
        Object bean = getBean();
        if (bean == null) { return newMap; }
        Class<?> beanClass = bean.getClass();
        Object clone = BeanUtils.beanToBean(bean, beanClass);
        newMap.setBean(clone);
        return newMap;
    }

}
