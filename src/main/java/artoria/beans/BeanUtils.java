package artoria.beans;

import artoria.converter.TypeConvertUtils;
import artoria.converter.TypeConverter;
import artoria.exception.ExceptionUtils;
import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.reflect.ReflectUtils;
import artoria.util.Assert;
import artoria.util.ObjectUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static artoria.common.Constants.THIRTY;

/**
 * Bean convert tools.
 * @author Kahle
 */
public class BeanUtils {
    private static final Class<? extends BeanMap> DEFAULT_MAP_TYPE = SimpleBeanMap.class;
    private static final TypeConverter CONVERTER_AGENT = new TypeConverterAgent();
    private static final BeanCopier DEFAULT_BEAN_COPIER = new SimpleBeanCopier();
    private static Logger log = LoggerFactory.getLogger(BeanUtils.class);
    private static Class<? extends BeanMap> mapType;
    private static BeanCopier beanCopier;

    private static class TypeConverterAgent implements TypeConverter {
        @Override
        public Object convert(Object source, Class<?> target) {

            return TypeConvertUtils.convert(source, target);
        }
    }

    public static Class<? extends BeanMap> getMapType() {

        return mapType != null ? mapType : DEFAULT_MAP_TYPE;
    }

    public static void setMapType(Class<? extends BeanMap> mapType) {
        Assert.notNull(mapType, "Parameter \"mapType\" must not null. ");
        if (mapType == BeanMap.class) {
            throw new IllegalArgumentException("Parameter \"mapType\" must not \"BeanMap.class\". ");
        }
        log.info("Set bean map type: {}", mapType.getName());
        BeanUtils.mapType = mapType;
    }

    public static BeanCopier getBeanCopier() {

        return beanCopier != null ? beanCopier : DEFAULT_BEAN_COPIER;
    }

    public static void setBeanCopier(BeanCopier beanCopier) {
        Assert.notNull(beanCopier, "Parameter \"beanCopier\" must not null. ");
        log.info("Set bean copier: {}", beanCopier.getClass().getName());
        BeanUtils.beanCopier = beanCopier;
    }

    public static BeanMap createBeanMap() {
        try {
            BeanMap beanMap = ReflectUtils.newInstance(getMapType());
            beanMap.setTypeConverter(CONVERTER_AGENT);
            return beanMap;
        }
        catch (Exception e) {
            throw ExceptionUtils.wrap(e);
        }
    }

    public static BeanMap createBeanMap(Object bean) {
        BeanMap beanMap = createBeanMap();
        beanMap.setBean(bean);
        return beanMap;
    }

    public static Object clone(Object obj) {
        Assert.notNull(obj, "Parameter \"obj\" must not null. ");
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

    public static void copy(Object from, Object to) {

        getBeanCopier().copy(from, to, CONVERTER_AGENT);
    }

    public static void copy(Object from, Object to, TypeConverter cvt) {

        getBeanCopier().copy(from, to, cvt);
    }

    public static <K, V> void copy(Object from, Map<K, V> to) {
        Assert.notNull(from, "Parameter \"from\" must not null. ");
        Assert.notNull(to, "Parameter \"to\" must not null. ");
        Map<K, V> map = ObjectUtils.cast(
                from instanceof Map ? from : createBeanMap(from)
        );
        to.putAll(map);
    }

    public static <K, V> void copy(Map<K, V> from, Object to) {

        BeanUtils.copy(from, to, CONVERTER_AGENT);
    }

    public static <K, V> void copy(Map<K, V> from, Object to, TypeConverter cvt) {
        Assert.notNull(from, "Parameter \"from\" must not null. ");
        Assert.notNull(to, "Parameter \"to\" must not null. ");
        BeanMap map = createBeanMap(to);
        map.setTypeConverter(cvt);
        map.putAll(from);
    }

    public static <F, T> T beanToBean(F from, T to) {
        if (from == null) { return null; }
        if (from instanceof Map) {
            Map<Object, Object> castMap = ObjectUtils.cast(from);
            BeanUtils.copy(castMap, to);
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
                Map<Object, Object> castMap = ObjectUtils.cast(from);
                BeanUtils.copy(castMap, to);
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

    public static <F> Map<String, Object> beanToMap(F from) {
        if (from == null) { return null; }
        Map<String, Object> result = new HashMap<String, Object>(THIRTY);
        BeanUtils.copy(from, result);
        return result;
    }

    public static <R, K, V> R mapToBean(Map<K, V> from, R to) {

        return BeanUtils.beanToBean(from, to);
    }

    public static <R, K, V> R mapToBean(Map<K, V> from, Class<R> toClass) {

        return BeanUtils.beanToBean(from, toClass);
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

    public static <F> List<Map<String, Object>> beanToMapInList(List<F> from) {
        if (from == null) { return null; }
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        for (F f : from) {
            Map<String, Object> map = BeanUtils.beanToMap(f);
            result.add(map);
        }
        return result;
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

}
