/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.data.dict.support;

import kunlun.cache.Cache;
import kunlun.common.Page;
import kunlun.common.constant.Nil;
import kunlun.data.dict.AbstractDictService;
import kunlun.data.dict.DataDict;
import kunlun.data.dict.DictQuery;
import kunlun.data.dict.DictService;
import kunlun.util.Assert;
import kunlun.util.StrUtil;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

/**
 * The cache data dictionary service.
 * @author Kahle
 */
public class CacheDictService extends AbstractDictService {
    private final DictService dictService;
    private final Cache    cache;
    private final Long     timeToLive;
    private final TimeUnit timeUnit;

    public CacheDictService(DictService dictService, Cache cache) {

        this(dictService, cache, Nil.INT3, Nil.<TimeUnit>g());
    }

    public CacheDictService(DictService dictService,
                            Cache cache,
                            Long timeToLive,
                            TimeUnit timeUnit) {
        this.dictService = Assert.notNull(dictService);
        this.cache = Assert.notNull(cache);
        this.timeToLive = timeToLive;
        this.timeUnit = timeUnit;
    }

    @Override
    public void syncByGroup(Collection<DataDict> data) {

        dictService.syncByGroup(data);
    }

    @Override
    public void syncByCode(Collection<DataDict> data) {

        dictService.syncByCode(data);
    }

    @Override
    public Page<DataDict> listByCondition(boolean paged, DictQuery condition) {
        Assert.notNull(condition, "Parameter \"condition\" must not null. ");
        if (paged || StrUtil.isBlank(condition.getGroupCode())) {
            return dictService.listByCondition(paged, condition);
        }
        String key = String.format("%s-%s:%s-%s-%s", condition.getNamespace(), condition.getGroupCode()
                , condition.getName(), condition.getCode(), condition.getValue());
        //noinspection unchecked
        Page<DataDict> val = (Page<DataDict>) cache.get(key);
        if (val != null) { return val; }
        synchronized (key.intern()) {
            //noinspection unchecked
            if ((val = (Page<DataDict>) cache.get(key)) != null) { return val; }
            val = dictService.listByCondition(paged, condition);
            if (val == null) { return null; }
            if (timeToLive != null && timeUnit != null) {
                cache.put(key, val, timeToLive, timeUnit);
            }
            else {
                cache.put(key, val);
            }
        }
        return val;
    }

}
