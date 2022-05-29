package artoria.cache;

import artoria.lang.ReferenceType;
import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.mock.MockUtils;
import artoria.test.bean.Book;
import artoria.util.ThreadUtils;
import com.alibaba.fastjson.JSON;
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
        CacheUtils.register(cache);
        SimpleCacheConfig cacheConfig1 = new SimpleCacheConfig(ReferenceType.WEAK, 2L);
        SimpleCache cache1 = new SimpleCache(cacheName1, cacheConfig1);
        CacheUtils.register(cache1);
        SimpleCache cache2 = new SimpleCache(cacheName2);
        CacheUtils.register(cache2);
    }

    @Test
    public void test1() {
        Book book = MockUtils.mock(Book.class);
        log.info(JSON.toJSONString(book));
        CacheUtils.put(DEFAULT, "test1", book);
        log.info(JSON.toJSONString(CacheUtils.get(DEFAULT, "test1")));
    }

    @Test
    public void testTimeToLive() {
        long timeToLive = TWO * ONE_HUNDRED;
        for (int i = ZERO; i < TEN; i++) {
            CacheUtils.put(cacheName, i, "test-data-" + i);
            CacheUtils.expire(cacheName, i, timeToLive, TimeUnit.MILLISECONDS);
        }
        log.info("Size: {}", CacheUtils.size(cacheName));
        ThreadUtils.sleepQuietly(timeToLive);
        CacheUtils.prune(cacheName);
        log.info("Size: {}", CacheUtils.size(cacheName));
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

    @Ignore
    @Test
    public void testConcurrentModificationException() {
        long start = System.currentTimeMillis();
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 1000000; i++) {
                    CacheUtils.put(cacheName2, i, i, 100, TimeUnit.MILLISECONDS);
                    ThreadUtils.sleepQuietly(0);
                }
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                ThreadUtils.sleepQuietly(100);
                for (int i = 0; i < 1000000; i++) {
                    CacheUtils.get(cacheName2, i);
                }
            }
        }).start();
        for (int i = 0; i < 1000000; i++) {
            CacheUtils.put(cacheName2, ">> "+i, i, 100, TimeUnit.MILLISECONDS);
        }
        long end = System.currentTimeMillis();
        log.info("Time spent: {}", (end - start) / 1000);
    }

}
