package artoria.fake;

import artoria.convert.type.TypeConvertUtils;
import artoria.exception.ExceptionUtils;
import artoria.reflect.ReflectUtils;
import artoria.util.*;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.util.*;

import static artoria.common.Constants.ZERO;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static java.util.Collections.emptyMap;

public class SimpleFaker extends AbstractFaker {
    private static final Integer DEFAULT_BOUND = 8192;
    private static final Integer DEFAULT_SIZE = 8;

    protected Map<String, Method> findWriteMethods(Class<?> clazz) {
        Assert.notNull(clazz, "Parameter \"clazz\" must not null. ");
        PropertyDescriptor[] descriptors = ReflectUtils.findPropertyDescriptors(clazz);
        Map<String, Method> methodMap = new HashMap<String, Method>(descriptors.length);
        for (PropertyDescriptor descriptor : descriptors) {
            Method writeMethod = descriptor.getWriteMethod();
            if (writeMethod != null) { methodMap.put(descriptor.getName(), writeMethod); }
        }
        return methodMap;
    }

    protected String judge(String expression, Class<?> clazz) {
        Assert.notNull(expression, "Parameter \"expression\" must not null. ");
        Assert.notNull(clazz, "Parameter \"clazz\" must not null. ");
        Class<?> wrapper = ClassUtils.getWrapper(clazz);
        expression = expression.toLowerCase();
        //
        boolean contains = expression.contains("id")
                && expression.endsWith("id")
                || expression.contains("identifier");
        boolean assignable = String.class.isAssignableFrom(wrapper)
                || Long.class.isAssignableFrom(wrapper);
        if (contains && assignable) { return "id"; }
        //
        expression = expression.toLowerCase();
        contains = expression.contains("name")
                && expression.endsWith("name");
        assignable = String.class.isAssignableFrom(clazz);
        if (contains && assignable) { return "name"; }
        //
        contains = expression.contains("time")
                || expression.contains("date")
                || expression.contains("timestamp");
        assignable = Date.class.isAssignableFrom(clazz)
                || java.sql.Date.class.isAssignableFrom(clazz)
                || Timestamp.class.isAssignableFrom(clazz);
        if (contains && assignable) { return "time"; }
        if (assignable) { return "time"; }
        //
        return expression;
    }

    protected Object fake(String attrName, Type paramType, boolean judge, Class<?> target) {
        Object val;
        if (paramType instanceof ParameterizedType) {
            ParameterizedType realType = (ParameterizedType) paramType;
            Class<?> rawType = (Class<?>) realType.getRawType();
            if (target.isAssignableFrom(rawType)) { return null; }
            //Type[] args = realType.getActualTypeArguments();
            //boolean hasArgs = args != null;
            boolean isMap = Map.class.isAssignableFrom(rawType);
            //isMap = isMap && hasArgs && args.length >= TWO;
            boolean isList = List.class.isAssignableFrom(rawType);
            //isList = isList && hasArgs && args.length >= ONE;
            boolean isArr = rawType.isArray();
            if (judge) { attrName = judge(attrName, rawType); }
            val = isMap || isList || isArr ? null : fake(attrName, rawType);
        }
        else {
            Class<?> paramClass = (Class<?>) paramType;
            if (target.isAssignableFrom(paramClass)) { return null; }
            if (judge) { attrName = judge(attrName, paramClass); }
            val = fake(attrName, paramClass);
        }
        return val;
    }

    public <T> T fake(Map<String, String> attrExpressMap, Class<T> clazz) {
        try {
            Assert.notNull(clazz, "Parameter \"clazz\" must not null. ");
            Assert.isFalse(isBasicType(clazz), "Parameter \"clazz\" must not be the basic type. ");
            if (attrExpressMap == null) { attrExpressMap = emptyMap(); }
            verifyUnsupportedClass(clazz);
            Map<String, Method> methodMap = findWriteMethods(clazz);
            T bean = ReflectUtils.newInstance(clazz);
            for (Map.Entry<String, Method> entry : methodMap.entrySet()) {
                String attrName = entry.getKey();
                Method method = entry.getValue();
                Type type = method.getGenericParameterTypes()[ZERO];
                String expression = attrExpressMap.get(attrName);
                Object val;
                if (StringUtils.isNotBlank(expression)) {
                    val = fake(expression, type, FALSE, clazz);
                }
                else {
                    val = fake(attrName, type, TRUE, clazz);
                }
                if (val != null) { method.invoke(bean, val); }
            }
            return bean;
        }
        catch (Exception e) {
            throw ExceptionUtils.wrap(e);
        }
    }

    @Override
    public String name() {

        return "simple";
    }

    @Override
    public <T> T fake(String expression, Class<T> clazz) {
        verifyParameters(expression, clazz);
        Class<?> wrapper = ClassUtils.getWrapper(clazz);
        if (Number.class.isAssignableFrom(wrapper)) {
            double nextDouble = RandomUtils.nextDouble() * DEFAULT_BOUND;
            nextDouble = NumberUtils.round(nextDouble).doubleValue();
            Object convert = TypeConvertUtils.convert(nextDouble, wrapper);
            return ObjectUtils.cast(convert, clazz);
        }
        if (Boolean.class.isAssignableFrom(wrapper)) {
            boolean nextBoolean = RandomUtils.nextBoolean();
            return ObjectUtils.cast(nextBoolean, clazz);
        }
        if (Character.class.isAssignableFrom(wrapper)) {
            Character nextCharacter = RandomUtils.nextCharacter();
            return ObjectUtils.cast(nextCharacter, clazz);
        }
        if (Date.class.isAssignableFrom(wrapper)) {
            Date nowDate = new Date();
            Object convert = TypeConvertUtils.convert(nowDate, wrapper);
            return ObjectUtils.cast(convert, clazz);
        }
        if (String.class.isAssignableFrom(wrapper)) {
            int size = RandomUtils.nextInt(DEFAULT_SIZE);
            String nextString = RandomUtils.nextString(++size);
            return ObjectUtils.cast(nextString, clazz);
        }
        if (Object.class.equals(wrapper)) {
            Object object = new Object();
            return ObjectUtils.cast(object, clazz);
        }
        return fake(Collections.<String, String>emptyMap(), clazz);
    }

}
