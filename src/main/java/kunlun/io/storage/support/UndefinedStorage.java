/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.io.storage.support;

import kunlun.io.storage.AbstractDataStorage;

/**
 * The undefined storage.
 * @author Kahle
 */
public class UndefinedStorage extends AbstractDataStorage {

    @Override
    public Object get(Object key) {

        throw new UnsupportedOperationException();
    }

    @Override
    public Object put(Object data) {

        throw new UnsupportedOperationException();
    }

    @Override
    public Object delete(Object key) {

        throw new UnsupportedOperationException();
    }

}
