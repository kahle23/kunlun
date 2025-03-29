/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.data.bean;

import kunlun.convert.ConversionService;
import kunlun.convert.ConversionUtil;
import kunlun.data.bean.support.SimpleBeanCopier;
import kunlun.data.bean.support.SimpleBeanMapFactory;
import kunlun.exception.ExceptionUtil;
import kunlun.logging.Logger;
import kunlun.logging.LoggerFactory;
import kunlun.util.Assert;
import kunlun.util.ObjUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static kunlun.common.constant.Numbers.THIRTY;

/**
 * The bean conversion tools.
 * @author Kahle
 */
public class BeanUtil {
    private static final Logger log = LoggerFactory.getLogger(BeanUtil.class);
    private static volatile BeanMapFactory beanMapFactory;
    private static volatile BeanCopier beanCopier;

    public static BeanMapFactory getBeanMapFactory() {
        if (beanMapFactory != null) { return beanMapFactory; }
        synchronized (BeanUtil.class) {
            if (beanMapFactory != null) { return beanMapFactory; }
            BeanUtil.setBeanMapFactory(new SimpleBeanMapFactory());
            return beanMapFactory;
        }
    }

    public static void setBeanMapFactory(BeanMapFactory beanMapFactory) {
        Assert.notNull(beanMapFactory, "Parameter \"beanMapFactory\" must not null. ");
        log.debug("Set bean map factory: {}", beanMapFactory.getClass().getName());
        BeanUtil.beanMapFactory = beanMapFactory;
    }

    public static BeanCopier getBeanCopier() {
        if (beanCopier != null) { return beanCopier; }
        synchronized (BeanUtil.class) {
            if (beanCopier != null) { return beanCopier; }
            BeanUtil.setBeanCopier(new SimpleBeanCopier());
            return beanCopier;
        }
    }

    public static void setBeanCopier(BeanCopier beanCopier) {
        Assert.notNull(beanCopier, "Parameter \"beanCopier\" must not null. ");
        log.debug("Set bean copier: {}", beanCopier.getClass().getName());
        BeanUtil.beanCopier = beanCopier;
    }

    public static BeanMap createBeanMap() {

        return getBeanMapFactory().getInstance(null);
    }

    public static BeanMap createBeanMap(Object bean) {

        return getBeanMapFactory().getInstance(bean);
    }

    public static void copy(Object from, Object to) {

        getBeanCopier().copy(from, to, ConversionUtil.getConversionService());
    }

    public static void copy(Object from, Object to, ConversionService conversionService) {

        getBeanCopier().copy(from, to, conversionService);
    }

    public static <K, V> void copy(Object from, Map<K, V> to) {
        Assert.notNull(from, "Parameter \"from\" must not null. ");
        Assert.notNull(to, "Parameter \"to\" must not null. ");
        Map<K, V> map = ObjUtil.cast(
                from instanceof Map ? from : createBeanMap(from)
        );
        to.putAll(map);
    }

    public static <K, V> void copy(Map<K, V> from, Object to) {

        copy(from, to, ConversionUtil.getConversionService());
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
            Map<Object, Object> castMap = ObjUtil.cast(from);
            copy(castMap, to);
        }
        else {
            copy(from, to);
        }
        return to;
    }

    public static <F, T> T beanToBean(F from, Class<T> toClass) {
        if (from == null) { return null; }
        try {
            T to = toClass.newInstance();
            if (from instanceof Map) {
                Map<Object, Object> castMap = ObjUtil.cast(from);
                copy(castMap, to);
            }
            else {
                copy(from, to);
            }
            return to;
        }
        catch (Exception e) {
            throw ExceptionUtil.wrap(e);
        }
    }

    public static <F> Map<String, Object> beanToMap(F from) {
        if (from == null) { return null; }
        if (from instanceof Map) { return ObjUtil.cast(from); }
        Map<String, Object> result = new HashMap<String, Object>(THIRTY);
        copy(from, result);
        return result;
    }

    public static <R, K, V> R mapToBean(Map<K, V> from, R to) {

        return beanToBean(from, to);
    }

    public static <R, K, V> R mapToBean(Map<K, V> from, Class<R> toClass) {

        return beanToBean(from, toClass);
    }

    public static <F, T> List<T> beanToBeanInList(List<F> from, Class<T> toClass) {
        if (from == null) { return null; }
        List<T> result = new ArrayList<T>();
        for (F f : from) {
            T bean = beanToBean(f, toClass);
            result.add(bean);
        }
        return result;
    }

    public static <F> List<Map<String, Object>> beanToMapInList(List<F> from) {
        if (from == null) { return null; }
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        for (F f : from) {
            Map<String, Object> map = beanToMap(f);
            result.add(map);
        }
        return result;
    }

    public static <R, K, V> List<R> mapToBeanInList(List<Map<K, V>> from, Class<R> toClass) {
        if (from == null) { return null; }
        List<R> result = new ArrayList<R>();
        for (Map<K, V> m : from) {
            R bean = mapToBean(m, toClass);
            result.add(bean);
        }
        return result;
    }

}
