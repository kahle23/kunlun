package artoria.beans;

import artoria.converter.TypeConvertUtils;
import artoria.converter.TypeConverter;
import artoria.exception.ExceptionUtils;
import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.reflect.ReflectUtils;
import artoria.util.Assert;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Bean convert tools.
 * @author Kahle
 */
public class BeanUtils {
    private static final Class<? extends BeanMap> DEFAULT_BEAN_MAP_CLASS = SimpleBeanMap.class;
    private static final BeanCopier DEFAULT_BEAN_COPIER = new SimpleBeanCopier();
    private static final TypeConverter COMMON_CONVERTER = new TypeConverter() {
        @Override
        public Object convert(Object source, Class<?> target) {

            return TypeConvertUtils.convert(source, target);
        }
    };
    private static final Integer MAP_INIT_CAPACITY = 32;
    private static Logger log = LoggerFactory.getLogger(BeanUtils.class);
    private static Class<? extends BeanMap> beanMapClass;
    private static BeanCopier beanCopier;

    public static Class<? extends BeanMap> getBeanMapClass() {
        return beanMapClass != null
                ? beanMapClass : DEFAULT_BEAN_MAP_CLASS;
    }

    public static void setBeanMapClass(Class<? extends BeanMap> beanMapClass) {
        Assert.notNull(beanMapClass,
                "Parameter \"beanMapClass\" must not null. ");
        Assert.state(beanMapClass != BeanMap.class,
                "Parameter \"beanMapClass\" must not \"BeanMap.class\". ");
        log.info("Set bean map class: " + beanMapClass.getName());
        BeanUtils.beanMapClass = beanMapClass;
    }

    public static BeanCopier getBeanCopier() {
        return beanCopier != null
                ? beanCopier : DEFAULT_BEAN_COPIER;
    }

    public static void setBeanCopier(BeanCopier beanCopier) {
        Assert.notNull(beanCopier,
                "Parameter \"beanCopier\" must not null. ");
        log.info("Set bean copier: " + beanCopier.getClass().getName());
        BeanUtils.beanCopier = beanCopier;
    }

    public static BeanMap createBeanMap() {
        try {
            Class<? extends BeanMap> beanMapClass = BeanUtils.getBeanMapClass();
            Constructor<?> cst = ReflectUtils.findConstructor(beanMapClass);
            return (BeanMap) cst.newInstance();
        }
        catch (Exception e) {
            throw ExceptionUtils.wrap(e);
        }
    }

    public static BeanMap createBeanMap(Object bean) {
        BeanMap map = BeanUtils.createBeanMap();
        map.setBean(bean);
        return map;
    }

    public static Object clone(Object obj) {
        try {
            Class<?> clazz = obj.getClass();
            Object clone = ReflectUtils.newInstance(clazz);
            BeanUtils.copy(obj, clone);
            return clone;
        }
        catch (Exception e) {
            throw ExceptionUtils.wrap(e);
        }
    }

    @SuppressWarnings("unchecked")
    public static void copy(Object from, Map to) {
        Assert.notNull(from, "Parameter \"from\" must not null. ");
        Assert.notNull(to, "Parameter \"to\" must not null. ");
        BeanMap map = BeanUtils.createBeanMap(from);
        to.putAll(map);
    }

    public static void copy(Map from, Object to) {

        BeanUtils.copy(from, to, COMMON_CONVERTER);
    }

    public static void copy(Map from, Object to, TypeConverter cvt) {
        Assert.notNull(from, "Parameter \"from\" must not null. ");
        Assert.notNull(to, "Parameter \"to\" must not null. ");
        BeanMap map = BeanUtils.createBeanMap(to);
        map.setTypeConverter(cvt);
        map.putAll(from);
    }

    public static void copy(Object from, Object to) {

        getBeanCopier().copy(from, to, null, COMMON_CONVERTER);
    }

    public static void copy(Object from, Object to, TypeConverter cvt) {

        getBeanCopier().copy(from, to, null, cvt);
    }

    public static void copy(Object from, Object to, List<String> ignore) {

        getBeanCopier().copy(from, to, ignore, COMMON_CONVERTER);
    }

    public static void copy(Object from, Object to, List<String> ignore, TypeConverter cvt) {

        getBeanCopier().copy(from, to, ignore, cvt);
    }

    public static <R, K, V> R mapToBean(Map<K, V> from, R to) {

        return BeanUtils.beanToBean(from, to);
    }

    public static <R, K, V> R mapToBean(Map<K, V> from, Class<R> toClass) {

        return BeanUtils.beanToBean(from, toClass);
    }

    public static <F> Map<String, Object> beanToMap(F from) {
        if (from == null) { return null; }
        Map<String, Object> result = new HashMap<String, Object>(MAP_INIT_CAPACITY);
        BeanUtils.copy(from, result);
        return result;
    }

    public static <F, T> T beanToBean(F from, T to) {
        if (from == null) { return null; }
        if (from instanceof Map) {
            BeanUtils.copy((Map) from, to);
        }
        else {
            BeanUtils.copy(from, to);
        }
        return to;
    }

    public static <F, T> T beanToBean(F from, Class<T> toClass) {
        if (from == null) { return null; }
        try {
            T to = toClass.newInstance();
            if (from instanceof Map) {
                BeanUtils.copy((Map) from, to);
            }
            else {
                BeanUtils.copy(from, to);
            }
            return to;
        }
        catch (Exception e) {
            throw ExceptionUtils.wrap(e);
        }
    }

    public static <R, K, V> List<R> mapToBeanInList(List<Map<K, V>> from, Class<R> toClass) {
        if (from == null) { return null; }
        List<R> result = new ArrayList<R>();
        for (Map<K, V> m : from) {
            R bean = BeanUtils.mapToBean(m, toClass);
            result.add(bean);
        }
        return result;
    }

    public static <F> List<Map<String, Object>> beanToMapInList(List<F> from) {
        if (from == null) { return null; }
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        for (F f : from) {
            Map<String, Object> map = BeanUtils.beanToMap(f);
            result.add(map);
        }
        return result;
    }

    public static <F, T> List<T> beanToBeanInList(List<F> from, Class<T> toClass) {
        if (from == null) { return null; }
        List<T> result = new ArrayList<T>();
        for (F f : from) {
            T bean = BeanUtils.beanToBean(f, toClass);
            result.add(bean);
        }
        return result;
    }

}
