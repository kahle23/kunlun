package com.github.kahlkn.artoria.util;

import org.junit.Test;

public class SimpleCacheTest {
    private static SimpleCache simpleCache = new SimpleCache();

    @Test
    public void testFunction() {
        DataLoader loader = new DataLoader() {
            @Override
            public Object load() {
                return System.currentTimeMillis();
            }
        };
        Object key = SimpleCacheTest.class + "testFunction";
        System.out.println(simpleCache.get(key, loader));
        System.out.println(simpleCache.get(key, loader));
        System.out.println(simpleCache.get(key, loader));
        System.gc();
        System.out.println(simpleCache.get(key, loader));
    }

    @Test
    public void testMultithreaded() {
        final DataLoader loader = new DataLoader() {
            @Override
            public Object load() {
                return System.currentTimeMillis();
            }
        };
        final Object key = SimpleCacheTest.class + "testMultithreaded";
        Thread threadGet = new Thread(new Runnable() {
            @Override
            public void run() {
                for (; ; ) {
                    ThreadUtils.sleepQuietly(200);
                    System.out.println(simpleCache.get(key, loader));
                }
            }
        });
        Thread threadRemove = new Thread(new Runnable() {
            @Override
            public void run() {
                for (; ; ) {
                    ThreadUtils.sleepQuietly(200);
                    simpleCache.remove(key);
                }
            }
        });
        threadGet.start();
        threadRemove.start();
        ThreadUtils.sleepQuietly(2000);
    }

}
