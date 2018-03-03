package com.apyhs.artoria.beans;

import com.apyhs.artoria.converter.Converter;

import java.util.Set;

/**
 * Cglib bean map.
 * @author Kahle
 */
public class CglibBeanMap extends BeanMap {

    private net.sf.cglib.beans.BeanMap beanMap;

    @Override
    public void setBean(Object bean) {
        super.setBean(bean);
        beanMap = net.sf.cglib.beans.BeanMap.create(bean);
    }

    @Override
    protected Object get(Object bean, Object key) {
        return beanMap.get(key);
    }

    @Override
    protected Object put(Object bean, Object key, Object value) {
        if (key != null) {
            Converter cvt = this.getConverter();
            Class type = beanMap.getPropertyType((String) key);
            value = cvt.convert(value, type);
        }
        return beanMap.put(key, value);
    }

    @Override
    public Set keySet() {
        return beanMap.keySet();
    }

}
