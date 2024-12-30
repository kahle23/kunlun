/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.data.dict;

import kunlun.common.constant.Nulls;

import java.lang.reflect.Type;
import java.util.List;

/**
 * The abstract data dictionary provider.
 * @author Kahle
 */
public abstract class AbstractDictService implements DictService {

    protected abstract Dict getDict(String group, String name, String code, String value);

    @Override
    public void sync(Object strategy, Object data) {

        throw new UnsupportedOperationException();
    }

    @Override
    public Dict getByName(String group, String name) {

        return getDict(group, name, Nulls.STR, Nulls.STR);
    }

    @Override
    public Dict getByCode(String group, String code) {

        return getDict(group, Nulls.STR, code, Nulls.STR);
    }

    @Override
    public Dict getByValue(String group, String value) {

        return getDict(group, Nulls.STR, Nulls.STR, value);
    }

    @Override
    public Dict getByCondition(DictQuery query) {

        return getDict(query.getGroup(), query.getName(), query.getCode(), query.getValue());
    }

}
