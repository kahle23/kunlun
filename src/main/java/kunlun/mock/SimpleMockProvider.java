/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.mock;

import kunlun.convert.ConversionUtils;
import kunlun.exception.ExceptionUtils;
import kunlun.reflect.ReflectUtils;
import kunlun.util.Assert;
import kunlun.util.ClassUtils;
import kunlun.util.NumberUtils;
import kunlun.util.RandomUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.*;
import java.util.*;

import static kunlun.common.constant.Numbers.*;

public class SimpleMockProvider implements MockProvider {
    /**
     * Between 0 and 'maxNestedCount'(include).
     */
    private Integer maxNestedCount;

    public SimpleMockProvider() {

        this(ONE);
    }

    public SimpleMockProvider(Integer maxNestedCount) {
        Assert.notNull(maxNestedCount, "Parameter \"maxNestedCount\" must not null. ");
        this.maxNestedCount = maxNestedCount >= ZERO && maxNestedCount < FOUR ? maxNestedCount : ONE;
    }

    protected Map<String, Method> findWriteMethods(Class<?> clazz) {
        Assert.notNull(clazz, "Parameter \"clazz\" must not null. ");
        PropertyDescriptor[] descriptors = ReflectUtils.getPropertyDescriptors(clazz);
        Map<String, Method> methodMap = new HashMap<String, Method>(descriptors.length);
        for (PropertyDescriptor descriptor : descriptors) {
            Method writeMethod = descriptor.getWriteMethod();
            if (writeMethod != null) { methodMap.put(descriptor.getName(), writeMethod); }
        }
        return methodMap;
    }

    protected Object mockAnyData(Type attrType, String attrName
            , Object[] arguments, int nested, Class<?> originalType, Type... genericTypes) {
        Assert.notNull(attrType, "Parameter \"attrType\" must not null. ");
        if (attrType instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) attrType;
            Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
            Type rawType = parameterizedType.getRawType();
            return mockAnyData(rawType, attrName, arguments, nested, originalType, actualTypeArguments);
        }
        else if (attrType instanceof GenericArrayType) {
            return mockArrayData(attrType, attrName, arguments, nested, originalType);
        }
        //else if (attrType instanceof TypeVariable) {
        //}
        else {
            return mockClassData((Class) attrType, attrName, arguments, nested, originalType, genericTypes);
        }
    }

    protected Object mockClassData(Class<?> attrType, String attrName
            , Object[] arguments, int nested, Class<?> originalType, Type... genericTypes) {
        Assert.notNull(attrType, "Parameter \"attrType\" must not null. ");
        Class<?> wrapper = ClassUtils.getWrapper(attrType);
        if (Number.class.isAssignableFrom(wrapper)) {
            double nextDouble = RandomUtils.nextDouble() * EIGHT_HUNDRED;
            nextDouble = NumberUtils.round(nextDouble).doubleValue();
            return ConversionUtils.convert(nextDouble, wrapper);
        }
        else if (Boolean.class.isAssignableFrom(wrapper)) {
            return RandomUtils.nextBoolean();
        }
        else if (Character.class.isAssignableFrom(wrapper)) {
            return RandomUtils.nextCharacter();
        }
        else if (Date.class.isAssignableFrom(wrapper)) {
            Date nowDate = new Date();
            return ConversionUtils.convert(nowDate, wrapper);
        }
        else if (String.class.isAssignableFrom(wrapper)) {
            int size = RandomUtils.nextInt(EIGHT);
            return RandomUtils.nextString(++size);
        }
        else if (Object.class.equals(wrapper)) {
            return new Object();
        }
        else if (wrapper.isEnum()) {
            return mockEnumData(wrapper, attrName, arguments, nested, originalType);
        }
        else if (wrapper.isArray()) {
            return mockArrayData(wrapper, attrName, arguments, nested, originalType);
        }
        else if (Map.class.isAssignableFrom(wrapper)) {
            return mockMapData(wrapper, attrName, arguments, nested, originalType, genericTypes);
        }
        else if (Collection.class.isAssignableFrom(wrapper)) {
            return mockCollectionData(wrapper, attrName, arguments, nested, originalType, genericTypes);
        }
        else {
            return mockBeanData(wrapper, attrName, arguments, nested, originalType, genericTypes);
        }
    }

    protected Object mockArrayData(Type attrType, String attrName
            , Object[] arguments, int nested, Class<?> originalType) {
        Assert.notNull(attrType, "Parameter \"attrType\" must not null. ");
        if (nested > maxNestedCount - ONE) { return null; }
        if (attrType instanceof GenericArrayType) {
            // Multidimensional arrays are not supported.
            return null;
        }
        int size = RandomUtils.nextInt(TEN);
        Class<?> componentClass = ((Class) attrType).getComponentType();
        Object result = Array.newInstance(componentClass, size);
        if (size == ZERO) { return result; }
        for (int index = ZERO; index < size; index++) {
            Object value = mockAnyData(componentClass, attrName, arguments, nested, originalType);
            Array.set(result, index, value);
        }
        return result;
    }

    protected Object mockEnumData(Class<?> attrType, String attrName
            , Object[] arguments, int nested, Class<?> originalType) {
        Assert.notNull(attrType, "Parameter \"attrType\" must not null. ");
        Enum[] enums = (Enum[]) attrType.getEnumConstants();
        if (enums.length == 0) {
            throw new IllegalStateException("Enum length is zero. ");
        }
        return enums[RandomUtils.nextInt(enums.length)];
    }

    protected Object mockMapData(Class<?> attrType, String attrName
            , Object[] arguments, int nested, Class<?> originalType, Type... genericTypes) {
        Assert.notNull(attrType, "Parameter \"attrType\" must not null. ");
        if (nested > maxNestedCount - ONE) { return null; }
        int size = RandomUtils.nextInt(TEN);
        Map<Object, Object> result;
        if (HashMap.class.isAssignableFrom(attrType)) {
            result = new HashMap<Object, Object>(size);
        }
        else if (LinkedHashMap.class.isAssignableFrom(attrType)) {
            result = new LinkedHashMap<Object, Object>(size);
        }
        else if (SortedMap.class.isAssignableFrom(attrType)) {
            result = new TreeMap<Object, Object>();
        }
        else {
            result = new HashMap<Object, Object>(size);
        }
        if (size == ZERO) { return result; }
        Type valueType = genericTypes[ONE];
        Type keyType = genericTypes[ZERO];
        for (int index = ZERO; index < size; index++) {
            Object valueObj = mockAnyData(valueType, attrName, arguments, nested, originalType);
            Object keyObj = mockAnyData(keyType, attrName, arguments, nested, originalType);
            result.put(keyObj, valueObj);
        }
        return result;
    }

    protected Object mockBeanData(Class<?> attrType, String attrName
            , Object[] arguments, int nested, Class<?> originalType, Type... genericTypes) {
        Assert.notNull(attrType, "Parameter \"attrType\" must not null. ");
        if (originalType != null && originalType.isAssignableFrom(attrType)) { nested++; }
        if (nested > maxNestedCount) { return null; }
        if (nested == ZERO) { originalType = attrType; }
        Map<String, Method> methodMap = findWriteMethods(attrType);
        try {
            Object bean = ReflectUtils.newInstance(attrType);
            for (Map.Entry<String, Method> entry : methodMap.entrySet()) {
                String attrNameKey = entry.getKey();
                Method method = entry.getValue();
                Type valueType = method.getGenericParameterTypes()[ZERO];
                Object value = mockAnyData(valueType, attrNameKey, arguments, nested, originalType);
                if (value != null) { method.invoke(bean, value); }
            }
            return bean;
        }
        catch (Exception e) {
            throw ExceptionUtils.wrap(e);
        }
    }

    protected Object mockCollectionData(Class<?> attrType, String attrName
            , Object[] arguments, int nested, Class<?> originalType, Type... genericTypes) {
        Assert.notNull(attrType, "Parameter \"attrType\" must not null. ");
        if (nested > maxNestedCount - ONE) { return null; }
        int size = RandomUtils.nextInt(TEN);
        Collection<Object> result;
        if (List.class.isAssignableFrom(attrType)) {
            result = new ArrayList<Object>(size);
        }
        else if (Set.class.isAssignableFrom(attrType)) {
            result = new HashSet<Object>(size);
        }
        else if (Queue.class.isAssignableFrom(attrType)) {
            result = new LinkedList<Object>();
        }
        else {
            result = new ArrayList<Object>(size);
        }
        if (size == ZERO) { return result; }
        Type objectType = genericTypes[ZERO];
        for (int index = ZERO; index < size; index++) {
            Object value = mockAnyData(objectType, attrName, arguments, nested, originalType);
            result.add(value);
        }
        return result;
    }

    @Override
    public Object mock(Type type, Object... arguments) {
        Assert.notNull(type, "Parameter \"type\" must not null. ");
        return mockAnyData(type, null, arguments, ZERO, null);
    }

}
