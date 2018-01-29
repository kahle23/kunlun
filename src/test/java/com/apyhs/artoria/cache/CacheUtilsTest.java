package com.apyhs.artoria.cache;

import com.apyhs.artoria.util.ThreadUtils;
import org.junit.Test;

public class CacheUtilsTest {
    private static CacheUtils cacheUtils = new CacheUtils();

    @Test
    public void testFunction() {
        cacheUtils.getCache().next(new SimpleCache());
        DataLoader loader = new DataLoader() {
            @Override
            public Object load() {
                return System.currentTimeMillis();
            }
        };
        Object key = CacheUtilsTest.class + "testFunction";
        System.out.println(cacheUtils.get(key, loader));
        System.out.println(cacheUtils.get(key, loader));
        System.out.println(cacheUtils.get(key, loader));
        System.gc();
        System.out.println(cacheUtils.get(key, loader));
    }

    @Test
    public void testMultithreaded() {
        final DataLoader loader = new DataLoader() {
            @Override
            public Object load() {
                return System.currentTimeMillis();
            }
        };
        final Object key = CacheUtilsTest.class + "testMultithreaded";
        Thread threadGet = new Thread(new Runnable() {
            @Override
            public void run() {
                for (; ; ) {
                    ThreadUtils.sleepQuietly(200);
                    System.out.println(cacheUtils.get(key, loader));
                }
            }
        });
        Thread threadRemove = new Thread(new Runnable() {
            @Override
            public void run() {
                for (; ; ) {
                    ThreadUtils.sleepQuietly(200);
                    cacheUtils.remove(key);
                }
            }
        });
        threadGet.start();
        threadRemove.start();
        ThreadUtils.sleepQuietly(2000);
    }

}
