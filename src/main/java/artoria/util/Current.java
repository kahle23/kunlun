package artoria.util;

import artoria.storage.StorageUtils;
import artoria.storage.support.ThreadLocalStorage;

import java.util.Collection;
import java.util.Map;

@Deprecated // TODO: 2022/11/19 Deletable
public class Current {
    public static final String CURRENT_NAME = "@Current";

    static {
        if (!StorageUtils.getStorages().containsKey(CURRENT_NAME)) {
            StorageUtils.register(new ThreadLocalStorage(CURRENT_NAME));
        }
    }

    public static <T> T get(Object key, Class<T> type) {

        return StorageUtils.get(CURRENT_NAME, key, type);
    }

    public static Object get(Object key) {

        return StorageUtils.get(CURRENT_NAME, key);
    }

    public static Object put(Object key, Object value) {

        return StorageUtils.put(CURRENT_NAME, key, value);
    }

    public static Object putAll(Map<?, ?> map) {

        return StorageUtils.putAll(CURRENT_NAME, map);
    }

    public static Object remove(Object key) {

        return StorageUtils.remove(CURRENT_NAME, key);
    }

    public static Object removeAll(Collection<?> keys) {

        return StorageUtils.removeAll(CURRENT_NAME, keys);
    }

    public static Object clear() {

        return StorageUtils.clear(CURRENT_NAME);
    }

}
