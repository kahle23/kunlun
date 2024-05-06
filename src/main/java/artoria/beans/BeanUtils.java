package artoria.beans;

import artoria.convert.ConversionService;
import artoria.convert.ConversionUtils;
import artoria.data.bean.BeanCopier;
import artoria.data.bean.BeanMap;
import artoria.data.bean.BeanMapFactory;
import artoria.logging.Logger;
import artoria.logging.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * The bean conversion tools.
 * @author Kahle
 */
@Deprecated // TODO: 2023/3/7 Deletable
public class BeanUtils {
    private static Logger log = LoggerFactory.getLogger(BeanUtils.class);

    public static BeanMapFactory getBeanMapFactory() {

        return artoria.data.bean.BeanUtils.getBeanMapFactory();
    }

    public static void setBeanMapFactory(BeanMapFactory beanMapFactory) {

        artoria.data.bean.BeanUtils.setBeanMapFactory(beanMapFactory);
    }

    public static BeanCopier getBeanCopier() {

        return artoria.data.bean.BeanUtils.getBeanCopier();
    }

    public static void setBeanCopier(BeanCopier beanCopier) {

        artoria.data.bean.BeanUtils.setBeanCopier(beanCopier);
    }

    public static BeanMap createBeanMap() {

        return artoria.data.bean.BeanUtils.createBeanMap();
    }

    public static BeanMap createBeanMap(Object bean) {

        return artoria.data.bean.BeanUtils.createBeanMap(bean);
    }

    public static void copy(Object from, Object to) {

        artoria.data.bean.BeanUtils.copy(from, to, ConversionUtils.getConversionService());
    }

    public static void copy(Object from, Object to, ConversionService conversionService) {

        artoria.data.bean.BeanUtils.copy(from, to, conversionService);
    }

    public static <K, V> void copy(Object from, Map<K, V> to) {

        artoria.data.bean.BeanUtils.copy(from, to);
    }

    public static <K, V> void copy(Map<K, V> from, Object to) {

        artoria.data.bean.BeanUtils.copy(from, to);
    }

    public static <K, V> void copy(Map<K, V> from, Object to, ConversionService conversionService) {

        artoria.data.bean.BeanUtils.copy(from, to, conversionService);
    }

    public static <F, T> T beanToBean(F from, T to) {

        return artoria.data.bean.BeanUtils.beanToBean(from, to);
    }

    public static <F, T> T beanToBean(F from, Class<T> toClass) {

        return artoria.data.bean.BeanUtils.beanToBean(from, toClass);
    }

    public static <F> Map<String, Object> beanToMap(F from) {

        return artoria.data.bean.BeanUtils.beanToMap(from);
    }

    public static <R, K, V> R mapToBean(Map<K, V> from, R to) {

        return artoria.data.bean.BeanUtils.beanToBean(from, to);
    }

    public static <R, K, V> R mapToBean(Map<K, V> from, Class<R> toClass) {

        return artoria.data.bean.BeanUtils.beanToBean(from, toClass);
    }

    public static <F, T> List<T> beanToBeanInList(List<F> from, Class<T> toClass) {

        return artoria.data.bean.BeanUtils.beanToBeanInList(from, toClass);
    }

    public static <F> List<Map<String, Object>> beanToMapInList(List<F> from) {

        return artoria.data.bean.BeanUtils.beanToMapInList(from);
    }

    public static <R, K, V> List<R> mapToBeanInList(List<Map<K, V>> from, Class<R> toClass) {

        return artoria.data.bean.BeanUtils.mapToBeanInList(from, toClass);
    }

}
