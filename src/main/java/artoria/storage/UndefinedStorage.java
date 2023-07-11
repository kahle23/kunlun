package artoria.storage;

/**
 * The undefined storage.
 * @author Kahle
 */
@Deprecated // TODO: 2022/11/19 Deletable
public class UndefinedStorage extends AbstractStorage {

    public UndefinedStorage(String name) {

        super(name);
    }

    @Override
    public <T> T get(Object key, Class<T> type) {

        throw new UnsupportedOperationException();
    }

    @Override
    public Object get(Object key) {

        throw new UnsupportedOperationException();
    }

    @Override
    public boolean containsKey(Object key) {

        throw new UnsupportedOperationException();
    }

    @Override
    public Object put(Object key, Object value) {

        throw new UnsupportedOperationException();
    }

    @Override
    public Object remove(Object key) {

        throw new UnsupportedOperationException();
    }

}
