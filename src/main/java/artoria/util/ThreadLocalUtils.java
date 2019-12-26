package artoria.util;

import java.util.HashMap;
import java.util.Map;

import static artoria.common.Constants.TWENTY;

/**
 * ThreadLocal tools.
 * @author Kahle
 */
public class ThreadLocalUtils {
    private static final ThreadLocal<HashMap<Object, Object>> THREAD_BUCKET = new ThreadLocal<HashMap<Object, Object>>();

    public static Object getValue(Object key) {
        Map<Object, Object> bucket = THREAD_BUCKET.get();
        if (bucket == null) { return null; }
        return bucket.get(key);
    }

    public static void setValue(Object key, Object value) {
        HashMap<Object, Object> bucket = THREAD_BUCKET.get();
        if (bucket == null) {
            bucket = new HashMap<Object, Object>(TWENTY);
            THREAD_BUCKET.set(bucket);
        }
        bucket.put(key, value);
    }

    public static void remove(Object key) {
        Map<Object, Object> bucket = THREAD_BUCKET.get();
        if (bucket == null) {
            return;
        }
        bucket.remove(key);
    }

    public static void clear() {

        THREAD_BUCKET.remove();
    }

}
