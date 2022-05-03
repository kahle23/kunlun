package artoria.data.dict;

import artoria.cache.CacheUtils;
import artoria.util.Assert;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * The cache data dictionary provider.
 * @author Kahle
 */
public class CacheDictProvider extends AbstractDictProvider {
    private final DictProvider dictProvider;
    private final String   cacheName;
    private final Long     timeToLive;
    private final TimeUnit timeUnit;

    public CacheDictProvider(DictProvider dictProvider, String cacheName) {

        this(dictProvider, cacheName, null, null);
    }

    public CacheDictProvider(DictProvider dictProvider,
                             String cacheName,
                             Long timeToLive,
                             TimeUnit timeUnit) {
        super(Collections.<String, Object>emptyMap());
        Assert.notNull(dictProvider, "Parameter \"dictProvider\" must not null. ");
        Assert.notBlank(cacheName, "Parameter \"cacheName\" must not blank. ");
        this.dictProvider = dictProvider;
        this.cacheName = cacheName;
        this.timeToLive = timeToLive;
        this.timeUnit = timeUnit;
    }

    @Override
    public void registerCommonProperties(Map<?, ?> commonProperties) {

        dictProvider.registerCommonProperties(commonProperties);
    }

    @Override
    public void clearCommonProperties() {

        dictProvider.clearCommonProperties();
    }

    @Override
    public Map<String, Object> getCommonProperties() {

        return dictProvider.getCommonProperties();
    }

    @Override
    public void sync(Object strategy, Object data) {

        dictProvider.sync(strategy, data);
    }

    protected Dict getDict(String group, String name, String code, String value) {
        Assert.notBlank(group, "Parameter \"group\" must not blank. ");
        String key = String.format("%s:%s-%s-%s", group, name, code, value);
        Dict val = (Dict) CacheUtils.get(cacheName, key);
        if (val != null) { return val; }
        synchronized (key.intern()) {
            if ((val = (Dict) CacheUtils.get(cacheName, key)) != null) { return val; }
            if (name != null) {
                val = dictProvider.getByName(group, name);
            }
            else if (code != null) {
                val = dictProvider.getByCode(group, code);
            }
            else {
                val = dictProvider.getByValue(group, value);
            }
            if (val == null) { return null; }
            if (timeToLive != null && timeUnit != null) {
                CacheUtils.put(cacheName, key, val, timeToLive, timeUnit);
            }
            else {
                CacheUtils.put(cacheName, key, val);
            }
        }
        return val;
    }

    @Override
    public Dict getByName(String group, String name) {

        return getDict(group, name, null, null);
    }

    @Override
    public Dict getByCode(String group, String code) {

        return getDict(group, null, code, null);
    }

    @Override
    public Dict getByValue(String group, String value) {

        return getDict(group, null, null, value);
    }

    @Override
    public Dict findOne(Object dictQuery) {

        return dictProvider.findOne(dictQuery);
    }

    @Override
    public <T> List<T> findList(Object dictQuery, Type dataType) {

        return dictProvider.findList(dictQuery, dataType);
    }

    /*@Override
    public List<Dict> getListByGroup(String group) {
        Assert.notBlank(group, "Parameter \"group\" must not blank. ");
        Object val = CacheUtils.get(cacheName, group);
        if (val != null) { return cast(val); }
        synchronized (group.intern()) {
            if ((val = CacheUtils.get(cacheName, group)) != null) { return cast(val); }
            val = dictProvider.getListByGroup(group);
            if (val == null) { return null; }
            if (timeToLive != null && timeUnit != null) {
                CacheUtils.put(cacheName, group, val, timeToLive, timeUnit);
            }
            else {
                CacheUtils.put(cacheName, group, val);
            }
        }
        return cast(val);
    }*/

}
