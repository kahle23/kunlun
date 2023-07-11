package artoria.storage.support;

import artoria.logging.Logger;
import artoria.logging.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

import static artoria.common.Constants.THIRTY;

/**
 * The thread local storage.
 * @author Kahle
 */
@Deprecated // TODO: 2022/11/19 Deletable
public class ThreadLocalStorage extends AbstractMapStorage {
    private static Logger log = LoggerFactory.getLogger(ThreadLocalStorage.class);
    private final ThreadLocal<Map<Object, Object>> threadBucket;

    public ThreadLocalStorage(String name) {
        super(name);
        threadBucket = new ThreadLocal<Map<Object, Object>>();
    }

    @Override
    protected Map<Object, Object> buildBucket() {

        return new HashMap<Object, Object>(THIRTY);
    }

    @Override
    protected Map<Object, Object> getBucket() {
        Map<Object, Object> bucket = threadBucket.get();
        if (bucket == null) {
            threadBucket.set(bucket = buildBucket());
        }
        return bucket;
    }

    @Override
    public ThreadLocal<Map<Object, Object>> getNative() {

        return threadBucket;
    }

    @Override
    public Object clear() {
        threadBucket.remove();
        return null;
    }

}
