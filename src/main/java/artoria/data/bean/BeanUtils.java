package artoria.data.bean;

import artoria.convert.ConversionService;
import artoria.convert.ConversionUtils;
import artoria.exception.ExceptionUtils;
import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.util.Assert;
import artoria.util.ObjectUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static artoria.common.constant.Numbers.THIRTY;

/**
 * The bean conversion tools.
 * @author Kahle
 */
public class BeanUtils {
    private static final Logger log = LoggerFactory.getLogger(BeanUtils.class);
    private static volatile BeanMapFactory beanMapFactory;
    private static volatile BeanCopier beanCopier;

    public static BeanMapFactory getBeanMapFactory() {
        if (beanMapFactory != null) { return beanMapFactory; }
        synchronized (BeanUtils.class) {
            if (beanMapFactory != null) { return beanMapFactory; }
            BeanUtils.setBeanMapFactory(new SimpleBeanMapFactory());
            return beanMapFactory;
        }
    }

    public static void setBeanMapFactory(BeanMapFactory beanMapFactory) {
        Assert.notNull(beanMapFactory, "Parameter \"beanMapFactory\" must not null. ");
        log.info("Set bean map factory: {}", beanMapFactory.getClass().getName());
        BeanUtils.beanMapFactory = beanMapFactory;
    }

    public static BeanCopier getBeanCopier() {
        if (beanCopier != null) { return beanCopier; }
        synchronized (BeanUtils.class) {
            if (beanCopier != null) { return beanCopier; }
            BeanUtils.setBeanCopier(new SimpleBeanCopier());
            return beanCopier;
        }
    }

    public static void setBeanCopier(BeanCopier beanCopier) {
        Assert.notNull(beanCopier, "Parameter \"beanCopier\" must not null. ");
        log.info("Set bean copier: {}", beanCopier.getClass().getName());
        BeanUtils.beanCopier = beanCopier;
    }

    public static BeanMap createBeanMap() {

        return getBeanMapFactory().getInstance(null);
    }

    public static BeanMap createBeanMap(Object bean) {

        return getBeanMapFactory().getInstance(bean);
    }

    public static void copy(Object from, Object to) {

        getBeanCopier().copy(from, to, ConversionUtils.getConversionService());
    }

    public static void copy(Object from, Object to, ConversionService conversionService) {

        getBeanCopier().copy(from, to, conversionService);
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

        BeanUtils.copy(from, to, ConversionUtils.getConversionService());
    }

    public static <K, V> void copy(Map<K, V> from, Object to, ConversionService conversionService) {
        Assert.notNull(from, "Parameter \"from\" must not null. ");
        Assert.notNull(to, "Parameter \"to\" must not null. ");
        BeanMap beanMap = createBeanMap(to);
        beanMap.setConversionService(conversionService);
        beanMap.putAll(from);
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
        if (from instanceof Map) { return ObjectUtils.cast(from); }
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
