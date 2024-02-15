package artoria.bean.support;

import artoria.bean.BeanManager;
import artoria.util.Assert;
import artoria.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static artoria.common.constant.Numbers.TWENTY;

/**
 * The simple bean manager.
 * @author Kahle
 */
public class SimpleBeanManager implements BeanManager {
    protected final Map<String, Object> beanMap;

    public SimpleBeanManager() {

        this(new ConcurrentHashMap<String, Object>());
    }

    protected SimpleBeanManager(Map<String, Object> beanMap) {
        Assert.notNull(beanMap, "Parameter \"beanMap\" must not null. ");
        this.beanMap = beanMap;
    }

    @Override
    public boolean contains(String name) {
        Assert.notBlank(name, "Parameter \"name\" must not blank. ");
        return beanMap.containsKey(name);
    }

    @Override
    public Object remove(String name) {
        Assert.notBlank(name, "Parameter \"name\" must not blank. ");
        return beanMap.remove(name);
    }

    @Override
    public Object get(String name) {
        Assert.notBlank(name, "Parameter \"name\" must not blank. ");
        return beanMap.get(name);
    }

    @Override
    public <T> T get(Class<T> type) {
        Assert.notNull(type, "Parameter \"type\" must not null. ");
        Object result = null;
        for (Object obj : beanMap.values()) {
            boolean assignable = type.isAssignableFrom(obj.getClass());
            Assert.isFalse(result != null && assignable
                    , "More than one bean of the given type was found. ");
            if (result == null && assignable) { result = obj; }
        }
        return ObjectUtils.cast(result);
    }

    @Override
    public <T> T get(String name, Class<T> type) {
        Object bean = get(name);
        if (bean == null) { return null; }
        Assert.isInstanceOf(type, bean
                , "The bean is not of the input type. ");
        return ObjectUtils.cast(bean);
    }

    @Override
    public Object put(String name, Object bean) {
        Assert.notBlank(name, "Parameter \"name\" must not blank. ");
        Assert.notNull(bean, "Parameter \"bean\" must not null. ");
        Assert.isFalse(beanMap.containsKey(name)
                , "Parameter \"name\" already exist. ");
        return beanMap.put(name, bean);
    }

    @Override
    public String[] getAliases(String name) {
        Assert.notBlank(name, "Parameter \"name\" must not blank. ");
        Object bean = get(name);
        if (bean == null) { return new String[]{}; }
        List<String> result = new ArrayList<String>();
        for (Map.Entry<String, Object> entry : beanMap.entrySet()) {
            Object value = entry.getValue();
            String key = entry.getKey();
            if (name.equals(key)) { continue; }
            if (bean.equals(value)) { result.add(key); }
        }
        return result.toArray(new String[]{});
    }

    @Override
    public String[] getNames(Class<?> type) {
        Assert.notNull(type, "Parameter \"type\" must not null. ");
        return getBeans(type).keySet().toArray(new String[]{});
    }

    @Override
    public <T> Map<String, T> getBeans(Class<T> type) {
        Assert.notNull(type, "Parameter \"type\" must not null. ");
        Map<String, T> result = new ConcurrentHashMap<String, T>(TWENTY);
        for (Map.Entry<String, Object> entry : beanMap.entrySet()) {
            Object value = entry.getValue();
            String key = entry.getKey();
            if (type.isAssignableFrom(value.getClass())) {
                T cast = ObjectUtils.cast(value);
                result.put(key, cast);
            }
        }
        return Collections.unmodifiableMap(result);
    }

}
