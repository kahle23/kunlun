package artoria.storage;

import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.storage.support.LocalFileStorage;
import artoria.util.Assert;
import artoria.util.ObjectUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static artoria.common.Constants.DEFAULT;

/**
 * The storage tools.
 * @author Kahle
 */
@Deprecated // TODO: 2023/3/23 Deletable
public class StorageUtils {
    private static final Map<String, Storage> STORAGES = new ConcurrentHashMap<String, Storage>();
    private static Logger log = LoggerFactory.getLogger(StorageUtils.class);

    public static void register(Storage storage) {
        Assert.notNull(storage, "Parameter \"storage\" must not null. ");
        String storageName = storage.getName();
        Assert.notBlank(storageName, "Parameter \"storageName\" must not blank. ");
        String storageClassName = storage.getClass().getName();
        log.info("Register \"{}\" to \"{}\". ", storageClassName, storageName);
        STORAGES.put(storageName, storage);
    }

    public static Storage deregister(String storageName) {
        Assert.notBlank(storageName, "Parameter \"storageName\" must not blank. ");
        Storage remove = STORAGES.remove(storageName);
        if (remove != null) {
            String removeClassName = remove.getClass().getName();
            log.info("Deregister \"{}\" to \"{}\". ", removeClassName, storageName);
        }
        return remove;
    }

    public static Map<String, Storage> getStorages() {

        return Collections.unmodifiableMap(STORAGES);
    }

    public static Storage getStorage(String storageName) {
        Assert.notBlank(storageName, "Parameter \"storageName\" must not blank. ");
        Storage storage = STORAGES.get(storageName);
        if (storage != null) { return storage; }
        if (DEFAULT.equals(storageName)) {
            register(storage = new LocalFileStorage(storageName));
        }
        else { register(storage = new UndefinedStorage(storageName)); }
        return storage;
    }

    public static <T> T getNative(String storageName, Class<T> type) {

        return ObjectUtils.cast(getStorage(storageName).getNative(), type);
    }

    public static <T> T get(String storageName, Object key, Class<T> type) {

        return getStorage(storageName).get(key, type);
    }

    public static Object get(String storageName, Object key) {

        return getStorage(storageName).get(key);
    }

    public static boolean containsKey(String storageName, Object key) {

        return getStorage(storageName).containsKey(key);
    }

    public static Object put(String storageName, Object key, Object value) {

        return getStorage(storageName).put(key, value);
    }

    public static Object putAll(String storageName, Map<?, ?> map) {

        return getStorage(storageName).putAll(map);
    }

    public static Object remove(String storageName, Object key) {

        return getStorage(storageName).remove(key);
    }

    public static Object removeAll(String storageName, Collection<?> keys) {

        return getStorage(storageName).removeAll(keys);
    }

    public static Object clear(String storageName) {

        return getStorage(storageName).clear();
    }

    public static <T> Collection<T> keys(String storageName, Object pattern, Class<T> type) {

        return getStorage(storageName).keys(pattern, type);
    }

}
