package artoria.io.storage;

import artoria.core.Storage;
import artoria.io.DataStorage;
import artoria.io.file.support.LocalFileStorage;
import artoria.io.storage.support.UndefinedStorage;
import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
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
public class StorageUtils {
    private static final Map<String, DataStorage> STORAGE_MAP = new ConcurrentHashMap<String, DataStorage>();
    private static final Logger log = LoggerFactory.getLogger(StorageUtils.class);

    public static void register(String name, DataStorage storage) {
        Assert.notNull(storage, "Parameter \"storage\" must not null. ");
        Assert.notBlank(name, "Parameter \"name\" must not blank. ");
        String storageClassName = storage.getClass().getName();
        log.info("Register \"{}\" to \"{}\". ", storageClassName, name);
        STORAGE_MAP.put(name, storage);
    }

    public static DataStorage deregister(String storageName) {
        Assert.notBlank(storageName, "Parameter \"storageName\" must not blank. ");
        DataStorage remove = STORAGE_MAP.remove(storageName);
        if (remove != null) {
            String removeClassName = remove.getClass().getName();
            log.info("Deregister \"{}\" to \"{}\". ", removeClassName, storageName);
        }
        return remove;
    }

    public static Map<String, DataStorage> getStorages() {

        return Collections.unmodifiableMap(STORAGE_MAP);
    }

    public static <T extends Storage> T getStorage(String name, Class<T> type) {

        return ObjectUtils.cast(getStorage(name), type);
    }

    public static DataStorage getStorage(String name) {
        Assert.notBlank(name, "Parameter \"name\" must not blank. ");
        DataStorage storage = STORAGE_MAP.get(name);
        if (storage != null) { return storage; }
        if (DEFAULT.equals(name)) {
            register(name, storage = new LocalFileStorage());
        }
        // What's the difference between reporting errors via "UndefinedStorage" and reporting errors directly here?
        else { register(name, storage = new UndefinedStorage()); }
        return storage;
    }

    public static boolean exist(String name, Object key) {

        return getStorage(name).exist(key);
    }

    public static <T> T  get(String name, Object key, Class<T> type) {

        return ObjectUtils.cast(getStorage(name).get(key), type);
    }

    public static Object get(String name, Object key) {

        return getStorage(name).get(key);
    }

    public static Object put(String name, Object data) {

        return getStorage(name).put(data);
    }

    public static Object delete(String name, Object key) {

        return getStorage(name).delete(key);
    }

    public static Object list(String name, Object conditions) {

        return getStorage(name).list(conditions);
    }

    public static Object put(String name, Object key, Object value) {

        return getStorage(name).put(key, value);
    }

    public static Object putAll(String name, Collection<?> data) {

        return getStorage(name).putAll(data);
    }

    public static Object deleteAll(String name, Collection<?> keys) {

        return getStorage(name).deleteAll(keys);
    }

    public static <T> Collection<T> list(String name, Object conditions, Class<T> type) {

        return ObjectUtils.cast(getStorage(name).list(conditions));
    }

}
