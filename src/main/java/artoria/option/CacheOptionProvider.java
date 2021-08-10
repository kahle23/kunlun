package artoria.option;

import artoria.cache.CacheUtils;
import artoria.util.Assert;
import artoria.util.ObjectUtils;
import artoria.util.StringUtils;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import static artoria.common.Constants.*;

public class CacheOptionProvider extends AbstractOptionProvider {
    private final OptionProvider optionProvider;
    private final String   cacheName;
    private final Long     timeToLive;
    private final TimeUnit timeUnit;

    public CacheOptionProvider(OptionProvider optionProvider,
                               String cacheName,
                               Long timeToLive,
                               TimeUnit timeUnit) {
        Assert.notNull(optionProvider, "Parameter \"optionProvider\" must not null. ");
        Assert.notBlank(cacheName, "Parameter \"cacheName\" must not blank. ");
        Assert.notNull(timeToLive, "Parameter \"timeToLive\" must not null. ");
        Assert.notNull(timeUnit, "Parameter \"timeUnit\" must not null. ");
        Assert.isTrue(timeToLive >= ZERO, "Parameter \"timeToLive\" must >= 0. ");
        this.optionProvider = optionProvider;
        this.cacheName = cacheName;
        this.timeToLive = timeToLive;
        this.timeUnit = timeUnit;
    }

    @Override
    public boolean containsOption(String owner, String name) {
        Assert.notBlank(name, "Parameter \"name\" must not blank. ");
        if (StringUtils.isBlank(owner)) { owner = EMPTY_STRING; }
        return optionProvider.containsOption(owner, name);
    }

    @Override
    public Map<String, Object> getOptions(String owner) {
        if (StringUtils.isBlank(owner)) { owner = EMPTY_STRING; }
        Object key = "options:" + owner;
        Object val = CacheUtils.get(cacheName, key);
        if (val == null) {
            val = optionProvider.getOptions(owner);
            if (val == null) { return null; }
            CacheUtils.put(cacheName, key, val, timeToLive, timeUnit);
        }
        return ObjectUtils.cast(val);
    }

    @Override
    public Object getOption(String owner, String name, Object defaultValue) {
        Assert.notBlank(name, "Parameter \"name\" must not blank. ");
        if (StringUtils.isBlank(owner)) { owner = EMPTY_STRING; }
        Object key = "option:" + owner + ":" + name;
        Object val = CacheUtils.get(cacheName, key);
        if (val == null) {
            val = optionProvider.getOption(owner, name, NULL_OBJ);
            if (val == null) { return defaultValue; }
            CacheUtils.put(cacheName, key, val, timeToLive, timeUnit);
        }
        return val;
    }

    @Override
    public Object setOption(String owner, String name, Object value) {
        Assert.notBlank(name, "Parameter \"name\" must not blank. ");
        Assert.notNull(value, "Parameter \"value\" must not null. ");
        if (StringUtils.isBlank(owner)) { owner = EMPTY_STRING; }
        Object old = optionProvider.setOption(owner, name, value);
        Object key = "option:" + owner + ":" + name;
        CacheUtils.remove(cacheName, key);
        return old;
    }

    @Override
    public Object removeOption(String owner, String name) {
        Assert.notBlank(name, "Parameter \"name\" must not blank. ");
        if (StringUtils.isBlank(owner)) { owner = EMPTY_STRING; }
        Object old = optionProvider.removeOption(owner, name);
        Object key = "option:" + owner + ":" + name;
        CacheUtils.remove(cacheName, key);
        return old;
    }

}
