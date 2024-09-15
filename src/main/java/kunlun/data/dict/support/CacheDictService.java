/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.data.dict.support;

import kunlun.cache.CacheUtils;
import kunlun.data.dict.AbstractDictService;
import kunlun.data.dict.Dict;
import kunlun.data.dict.DictService;
import kunlun.util.Assert;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

/**
 * The cache data dictionary service.
 * @author Kahle
 */
public class CacheDictService extends AbstractDictService {
    private final DictService dictService;
    private final String   cacheName;
    private final Long     timeToLive;
    private final TimeUnit timeUnit;

    public CacheDictService(DictService dictService, String cacheName) {

        this(dictService, cacheName, null, null);
    }

    public CacheDictService(DictService dictService,
                            String cacheName,
                            Long timeToLive,
                            TimeUnit timeUnit) {
        Assert.notNull(dictService, "Parameter \"dictService\" must not null. ");
        Assert.notBlank(cacheName, "Parameter \"cacheName\" must not blank. ");
        this.dictService = dictService;
        this.cacheName = cacheName;
        this.timeToLive = timeToLive;
        this.timeUnit = timeUnit;
    }

    @Override
    protected Dict getDict(String group, String name, String code, String value) {
        Assert.notBlank(group, "Parameter \"group\" must not blank. ");
        String key = String.format("%s:%s-%s-%s", group, name, code, value);
        Dict val = (Dict) CacheUtils.get(cacheName, key);
        if (val != null) { return val; }
        synchronized (key.intern()) {
            if ((val = (Dict) CacheUtils.get(cacheName, key)) != null) { return val; }
            if (name != null) {
                val = dictService.getByName(group, name);
            }
            else if (code != null) {
                val = dictService.getByCode(group, code);
            }
            else {
                val = dictService.getByValue(group, value);
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
    public void sync(Object strategy, Object data) {

        dictService.sync(strategy, data);
    }

    @Override
    public Dict getByCondition(DictQuery condition) {

        return dictService.getByCondition(condition);
    }

    @Override
    public Collection<Dict> listByGroup(String group) {

        return dictService.listByGroup(group);
    }

    @Override
    public Collection<Dict> listByCondition(DictQuery condition) {

        return dictService.listByCondition(condition);
    }

    /*@Override
    public List<Dict> listByGroup(String group) {
        Assert.notBlank(group, "Parameter \"group\" must not blank. ");
        Object val = CacheUtils.get(cacheName, group);
        if (val != null) { return cast(val); }
        synchronized (group.intern()) {
            if ((val = CacheUtils.get(cacheName, group)) != null) { return cast(val); }
            val = dictService.getListByGroup(group);
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
