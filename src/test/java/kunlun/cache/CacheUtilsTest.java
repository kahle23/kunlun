/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.cache;

import com.alibaba.fastjson.JSON;
import kunlun.cache.support.SimpleCache;
import kunlun.cache.support.SimpleCacheConfig;
import kunlun.data.ReferenceType;
import kunlun.data.mock.MockUtils;
import kunlun.logging.Logger;
import kunlun.logging.LoggerFactory;
import kunlun.test.pojo.entity.other.Book;
import kunlun.util.ThreadUtils;
import org.junit.Ignore;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static kunlun.common.constant.Numbers.*;
import static kunlun.common.constant.Words.DEFAULT;

/**
 * The cache tools Test.
 * @author Kahle
 */
public class CacheUtilsTest {
    private static final Logger log = LoggerFactory.getLogger(CacheUtilsTest.class);
    private static final String cacheName = "TEST";
    private static final String cacheName1 = "TEST1";
    private static final String cacheName2 = "TEST2";

    static {
        SimpleCache cache = new SimpleCache();
        CacheUtils.registerCache(cacheName, cache);
        SimpleCache cache1 = new SimpleCache(
                new SimpleCacheConfig(ReferenceType.WEAK, 2L));
        CacheUtils.registerCache(cacheName1, cache1);
        SimpleCache cache2 = new SimpleCache();
        CacheUtils.registerCache(cacheName2, cache2);
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
