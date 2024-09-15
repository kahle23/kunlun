/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.data.dict;

import kunlun.common.constant.Nulls;

/**
 * The abstract data dictionary service.
 * @author Kahle
 */
public abstract class AbstractDictService implements DictService {

    /**
     * Get the dict object by the condition.
     * @param group The dictionary item group information
     * @param name The dictionary item name
     * @param code The dictionary item code
     * @param value The dictionary item value
     * @return The dictionary item or null
     */
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
    public Dict getByCondition(DictQuery c) {

        return getDict(c.getGroup(), c.getName(), c.getCode(), c.getValue());
    }

}
