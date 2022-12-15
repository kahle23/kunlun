package artoria.io.storage;

import artoria.core.Storage;

/**
 * The undefined storage.
 * @author Kahle
 */
public class UndefinedStorage extends AbstractStorage implements Storage, NormalStorage {

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
