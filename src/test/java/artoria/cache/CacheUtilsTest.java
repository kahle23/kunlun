package artoria.cache;

import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.util.ThreadUtils;
import org.junit.Ignore;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static artoria.common.Constants.*;

public class CacheUtilsTest {
    private static Logger log = LoggerFactory.getLogger(CacheUtilsTest.class);
    private static String cacheName = "TEST";
    private static String cacheName1 = "TEST1";
    private static String cacheName2 = "TEST2";

    static {
        SimpleCache cache = new SimpleCache(cacheName);
        cache.setPrintLog(true);
        CacheUtils.register(cache);
        SimpleCache cache1 = new SimpleCache(cacheName1, TWO);
        cache1.setPrintLog(true);
        CacheUtils.register(cache1);
        SimpleCache cache2 = new SimpleCache(cacheName2);
        cache2.setPrintLog(true);
        CacheUtils.register(cache2);
    }

    @Test
    public void testTimeToLive() {
        long timeToLive = TWO * ONE_HUNDRED;
        for (int i = ZERO; i < TEN; i++) {
            CacheUtils.put(cacheName, i, "test-data-" + i);
            CacheUtils.expire(cacheName, i, timeToLive, TimeUnit.MILLISECONDS);
        }
        ThreadUtils.sleepQuietly(ONE_THOUSAND);
        for (int i = ZERO; i < TEN; i++) {
            log.info("{}", CacheUtils.get(cacheName, i));
        }
    }

    @Test
    public void testCapacity() {
        try {
            CacheUtils.put(cacheName1, "key1", "val1");
            CacheUtils.put(cacheName1, "key2", "val2");
            CacheUtils.put(cacheName1, "key3", "val3");
            log.info("{}", CacheUtils.get(cacheName1, "key3"));
        }
        catch (IllegalStateException e) {
            log.info(e.getMessage(), e);
        }
    }

    @Ignore
    @Test
    public void testWeakReferenceCache() {
        StringBuilder builder = new StringBuilder();
        for (int i = ZERO; i < 1000; i++) {
            builder.append("data-").append(i).append("-data-");
        }
        for (int i = ZERO; i <= 9999999; i++) {
            CacheUtils.put(cacheName2, i, builder.toString());
            CacheUtils.get(cacheName2, i);
        }
    }

}
