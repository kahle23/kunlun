package artoria.util;

import artoria.converter.ConvertUtils;
import artoria.exception.ExceptionUtils;
import artoria.reflect.ReflectUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static artoria.util.Const.GET;

/**
 * Data handle tools.
 * @author Kahle
 */
public class DataUtils {

    /**
     * A java bean list to divide into groups.
     * @param list A java bean list
     * @param groupSize Grouping size
     * @param <T> Java bean type
     * @return A list bean list
     */
    public static <T> List<List<T>> listToListList(List<T> list, int groupSize) {
        List<List<T>> result = new ArrayList<List<T>>();
        if (CollectionUtils.isEmpty(list)) { return result; }
        Assert.state(groupSize != 0, "Parameter \"groupSize\" must not equal 0. ");
        int listSize = list.size();
        int count = listSize / groupSize;
        count = listSize % groupSize == 0 ? count : ++count;
        List<T> tmp;
        for (int i = 0; i < count; i++) {
            int fromIndex = i * groupSize;
            int toIndex = (i + 1) * groupSize;
            toIndex = toIndex > listSize ? listSize : toIndex;
            tmp = new ArrayList<T>();
            tmp.addAll(list.subList(fromIndex, toIndex));
            result.add(tmp);
        }
        return result;
    }

    /**
     * A java bean list to convert a java bean property list.
     * @param list A java bean list
     * @param propertyName Java bean property name
     * @param propertyClass Java bean property type
     * @param <R> Java bean property type
     * @param <P> Java bean type
     * @return Java bean property list
     */
    @SuppressWarnings("unchecked")
    public static <R, P> List<R> listToListProperty(List<P> list, String propertyName, Class<R> propertyClass) {
        try {
            // Handle parameters
            List<R> result = new ArrayList<R>();
            if (CollectionUtils.isEmpty(list)) { return result; }
            Assert.notBlank(propertyName, "Parameter \"propertyName\" must not blank. ");
            Assert.notNull(propertyClass, "Parameter \"propertyClass\" must not null. ");
            P tmp = CollectionUtils.takeFirstNotNullElement(list);
            Assert.notNull(tmp, "Elements in list all is null. ");
            Method method = DataUtils.findMethodByName(tmp.getClass(), propertyName);
            // Handle result
            for (P bean : list) {
                if (bean == null) { continue; }
                Object val = method.invoke(bean);
                val = ConvertUtils.convert(val, propertyClass);
                result.add((R) val);
            }
            return result;
        }
        catch (Exception e) {
            throw ExceptionUtils.wrap(e);
        }
    }

    /**
     * A java bean list to convert a map who key is bean properties and value is bean.
     * This means if bean property is repeated and value will override.
     * @param list A java bean list
     * @param propertyNames Method property names
     * @param <T> Java bean type
     * @return A map key is bean property and value is bean
     */
    public static <T> Map<String, T> listToMapBean(List<T> list, String... propertyNames) {
        try {
            // Handle parameters
            Map<String, T> result = new HashMap<String, T>(list.size());
            if (CollectionUtils.isEmpty(list)) { return result; }
            Assert.notEmpty(propertyNames, "Parameter \"propertyNames\" must not empty. ");
            T tmp = CollectionUtils.takeFirstNotNullElement(list);
            Assert.notNull(tmp, "Elements in list all is null. ");
            List<Method> methods = DataUtils.findMethodsByNames(tmp.getClass(), propertyNames);
            // Convert to map
            StringBuilder key = new StringBuilder();
            for (T bean : list) {
                if (bean == null) { continue; }
                key.setLength(0);
                for (Method method : methods) {
                    Object val = method.invoke(bean);
                    key.append(val);
                }
                result.put(key.toString(), bean);
            }
            return result;
        }
        catch (Exception e) {
            throw ExceptionUtils.wrap(e);
        }
    }

    /**
     * A java bean list to convert a map who key is bean properties and value is bean list.
     * This means bean property in bean list have repeated.
     * @param list A java bean list
     * @param propertyNames Method property names
     * @param <T> Java bean type
     * @return A map key is bean property and value is bean list
     */
    public static <T> Map<String, List<T>> listToMapList(List<T> list, String... propertyNames) {
        try {
            // Handle parameters
            Map<String, List<T>> result = new HashMap<String, List<T>>(list.size());
            if (CollectionUtils.isEmpty(list)) { return result; }
            Assert.notEmpty(propertyNames, "Parameter \"propertyNames\" must not empty. ");
            T tmp = CollectionUtils.takeFirstNotNullElement(list);
            Assert.notNull(tmp, "Elements in list all is null. ");
            List<Method> methods = DataUtils.findMethodsByNames(tmp.getClass(), propertyNames);
            // Convert to map
            StringBuilder builder = new StringBuilder();
            for (T bean : list) {
                if (bean == null) { continue; }
                builder.setLength(0);
                for (Method method : methods) {
                    Object val = method.invoke(bean);
                    builder.append(val);
                }
                String key = builder.toString();
                List<T> val = result.get(key);
                if (val == null) {
                    val = new ArrayList<T>();
                    result.put(key, val);
                }
                val.add(bean);
            }
            return result;
        }
        catch (Exception e) {
            throw ExceptionUtils.wrap(e);
        }
    }

    /**
     * A java bean list to convert a map who key is bean properties and value is bean property.
     * @param list A java bean list
     * @param valueProperty Map's value corresponding method property names
     * @param keyProperties Map's key corresponding method properties names
     * @param <R> Map's value type
     * @param <P> Java bean type
     * @return A map key is bean properties and value is bean property
     */
    @SuppressWarnings("unchecked")
    public static <R, P> Map<String, R> listToMapProperty(List<P> list, String valueProperty, String... keyProperties) {
        try {
            // Handle parameters
            Map<String, R> result = new HashMap<String, R>(list.size());
            if (CollectionUtils.isEmpty(list)) { return result; }
            Assert.notBlank(valueProperty, "Parameter \"valueProperty\" must not blank. ");
            Assert.notEmpty(keyProperties, "Parameter \"keyProperties\" must not empty. ");
            P tmp = CollectionUtils.takeFirstNotNullElement(list);
            Assert.notNull(tmp, "Elements in list all is null. ");
            Class<?> tmpClass = tmp.getClass();
            Method valMethod = DataUtils.findMethodByName(tmpClass, valueProperty);
            List<Method> keyMethods = DataUtils.findMethodsByNames(tmp.getClass(), keyProperties);
            // Convert to map
            StringBuilder keyBuilder = new StringBuilder();
            for (P bean : list) {
                if (bean == null) { continue; }
                keyBuilder.setLength(0);
                for (Method method : keyMethods) {
                    Object key = method.invoke(bean);
                    keyBuilder.append(key);
                }
                R val = (R) valMethod.invoke(bean);
                result.put(keyBuilder.toString(), val);
            }
            return result;
        }
        catch (Exception e) {
            throw ExceptionUtils.wrap(e);
        }
    }

    /**
     * Find Method object by method name.
     * @param clazz Method come from class
     * @param propertyName Method property name
     * @return Method object
     * @throws NoSuchMethodException No such method
     */
    private static Method findMethodByName(Class<?> clazz, String propertyName) throws NoSuchMethodException {
        String methodName = GET + StringUtils.capitalize(propertyName);
        return ReflectUtils.findMethod(clazz, methodName);
    }

    /**
     * Find Method objects by method names.
     * @param clazz Method come from class
     * @param propertyNames Method property names
     * @return Method list
     * @throws NoSuchMethodException No such method
     */
    private static List<Method> findMethodsByNames(Class<?> clazz, String... propertyNames) throws NoSuchMethodException {
        List<Method> methods = new ArrayList<Method>();
        for (String name : propertyNames) {
            methods.add(DataUtils.findMethodByName(clazz, name));
        }
        return methods;
    }

}
