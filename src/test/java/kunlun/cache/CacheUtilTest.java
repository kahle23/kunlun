/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.cache;

import com.alibaba.fastjson.JSON;
import kunlun.cache.support.SimpleCache;
import kunlun.cache.support.SimpleCacheConfig;
import kunlun.data.ReferenceType;
import kunlun.data.mock.MockUtil;
import kunlun.logging.Logger;
import kunlun.logging.LoggerFactory;
import kunlun.test.pojo.entity.other.Book;
import kunlun.util.ThreadUtil;
import org.junit.Ignore;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static kunlun.common.constant.Numbers.*;
import static kunlun.common.constant.Words.DEFAULT;

/**
 * The cache tools Test.
 * @author Kahle
 */
public class CacheUtilTest {
    private static final Logger log = LoggerFactory.getLogger(CacheUtilTest.class);
    private static final String cacheName = "TEST";
    private static final String cacheName1 = "TEST1";
    private static final String cacheName2 = "TEST2";

    static {
        SimpleCache cache = new SimpleCache();
        CacheUtil.registerCache(cacheName, cache);
        SimpleCache cache1 = new SimpleCache(
                new SimpleCacheConfig(ReferenceType.WEAK, 2L));
        CacheUtil.registerCache(cacheName1, cache1);
        SimpleCache cache2 = new SimpleCache();
        CacheUtil.registerCache(cacheName2, cache2);
    }

    @Test
    public void test1() {
        Book book = MockUtil.mock(Book.class);
        log.info(JSON.toJSONString(book));
        CacheUtil.put(DEFAULT, "test1", book);
        log.info(JSON.toJSONString(CacheUtil.get(DEFAULT, "test1")));
    }

    @Test
    public void testTimeToLive() {
        long timeToLive = TWO * ONE_HUNDRED;
        for (int i = ZERO; i < TEN; i++) {
            CacheUtil.put(cacheName, i, "test-data-" + i);
            CacheUtil.expire(cacheName, i, timeToLive, TimeUnit.MILLISECONDS);
        }
        log.info("Size: {}", CacheUtil.size(cacheName));
        ThreadUtil.sleepQuietly(timeToLive);
        CacheUtil.prune(cacheName);
        log.info("Size: {}", CacheUtil.size(cacheName));
        for (int i = ZERO; i < TEN; i++) {
            log.info("{}", CacheUtil.get(cacheName, i));
        }
    }

    @Test
    public void testCapacity() {
        try {
            CacheUtil.put(cacheName1, "key1", "val1");
            CacheUtil.put(cacheName1, "key2", "val2");
            CacheUtil.put(cacheName1, "key3", "val3");
            log.info("{}", CacheUtil.get(cacheName1, "key3"));
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
            CacheUtil.put(cacheName2, i, builder.toString());
            CacheUtil.get(cacheName2, i);
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
                    CacheUtil.put(cacheName2, i, i, 100, TimeUnit.MILLISECONDS);
                    ThreadUtil.sleepQuietly(0);
                }
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                ThreadUtil.sleepQuietly(100);
                for (int i = 0; i < 1000000; i++) {
                    CacheUtil.get(cacheName2, i);
                }
            }
        }).start();
        for (int i = 0; i < 1000000; i++) {
            CacheUtil.put(cacheName2, ">> "+i, i, 100, TimeUnit.MILLISECONDS);
        }
        long end = System.currentTimeMillis();
        log.info("Time spent: {}", (end - start) / 1000);
    }

}
