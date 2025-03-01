/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.data.bean;

import kunlun.convert.ConversionService;
import kunlun.logging.Logger;
import kunlun.logging.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * The bean conversion tools.
 * @author Kahle
 */
@Deprecated
public class BeanUtils {
    private static final Logger log = LoggerFactory.getLogger(BeanUtils.class);

    public static BeanMapFactory getBeanMapFactory() {

        return BeanUtil.getBeanMapFactory();
    }

    public static void setBeanMapFactory(BeanMapFactory beanMapFactory) {

        BeanUtil.setBeanMapFactory(beanMapFactory);
    }

    public static BeanCopier getBeanCopier() {

        return BeanUtil.getBeanCopier();
    }

    public static void setBeanCopier(BeanCopier beanCopier) {

        BeanUtil.setBeanCopier(beanCopier);
    }

    public static BeanMap createBeanMap() {

        return BeanUtil.createBeanMap();
    }

    public static BeanMap createBeanMap(Object bean) {

        return BeanUtil.createBeanMap(bean);
    }

    public static void copy(Object from, Object to) {

        BeanUtil.copy(from, to);
    }

    public static void copy(Object from, Object to, ConversionService conversionService) {

        BeanUtil.copy(from, to, conversionService);
    }

    public static <K, V> void copy(Object from, Map<K, V> to) {

        BeanUtil.copy(from, to);
    }

    public static <K, V> void copy(Map<K, V> from, Object to) {

        BeanUtil.copy(from, to);
    }

    public static <K, V> void copy(Map<K, V> from, Object to, ConversionService conversionService) {

        BeanUtil.copy(from, to, conversionService);
    }

    public static <F, T> T beanToBean(F from, T to) {

        return BeanUtil.beanToBean(from, to);
    }

    public static <F, T> T beanToBean(F from, Class<T> toClass) {

        return BeanUtil.beanToBean(from, toClass);
    }

    public static <F> Map<String, Object> beanToMap(F from) {

        return BeanUtil.beanToMap(from);
    }

    public static <R, K, V> R mapToBean(Map<K, V> from, R to) {

        return BeanUtil.mapToBean(from, to);
    }

    public static <R, K, V> R mapToBean(Map<K, V> from, Class<R> toClass) {

        return BeanUtil.mapToBean(from, toClass);
    }

    public static <F, T> List<T> beanToBeanInList(List<F> from, Class<T> toClass) {

        return BeanUtil.beanToBeanInList(from, toClass);
    }

    public static <F> List<Map<String, Object>> beanToMapInList(List<F> from) {

        return BeanUtil.beanToMapInList(from);
    }

    public static <R, K, V> List<R> mapToBeanInList(List<Map<K, V>> from, Class<R> toClass) {

        return BeanUtil.mapToBeanInList(from, toClass);
    }

}
