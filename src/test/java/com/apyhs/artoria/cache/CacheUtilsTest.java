package com.apyhs.artoria.cache;

import com.apyhs.artoria.util.ThreadUtils;
import org.junit.Test;

public class CacheUtilsTest {

    @Test
    public void testFunction() {
        CacheUtils.getCache().next(new WeakSynCache());
        DataLoader loader = new DataLoader() {
            @Override
            public Object load() {
                return System.currentTimeMillis();
            }
        };
        Object key = CacheUtilsTest.class + "testFunction";
        System.out.println(CacheUtils.get(key, loader));
        System.out.println(CacheUtils.get(key, loader));
        System.out.println(CacheUtils.get(key, loader));
        System.gc();
        System.out.println(CacheUtils.get(key, loader));
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
                    System.out.println(CacheUtils.get(key, loader));
                }
            }
        });
        Thread threadRemove = new Thread(new Runnable() {
            @Override
            public void run() {
                for (; ; ) {
                    ThreadUtils.sleepQuietly(200);
                    CacheUtils.remove(key);
                }
            }
        });
        threadGet.start();
        threadRemove.start();
        ThreadUtils.sleepQuietly(2000);
    }

}
