package artoria.io.storage.support;

import artoria.io.storage.AbstractDataStorage;

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
