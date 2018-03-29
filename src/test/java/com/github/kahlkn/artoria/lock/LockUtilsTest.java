package com.github.kahlkn.artoria.lock;

import com.github.kahlkn.artoria.util.ThreadUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;

public class LockUtilsTest {
    private ExecutorService pool;
    private Integer num = 100;

    @Before
    public void init() {
        ThreadFactory threadFactory = Executors.defaultThreadFactory();
        pool = new ThreadPoolExecutor(10, 10, 0L
                , TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(), threadFactory);
        // Must register lock before use
        LockUtils.registerLock(LockUtilsTest.class.getName(), ReentrantLock.class);
    }

    @After
    public void destroy() {
        pool.shutdown();
        // Unregister lock is better
        LockUtils.unregisterLock(LockUtilsTest.class.getName());
    }

    @Test
    public void testRegisterFactory() {
        SimpleLockFactory lockFactory = new SimpleLockFactory();
        LockUtils.registerFactory(ReentrantLock.class, lockFactory);
    }

    @Test
    public void testUnregisterFactory() {
        LockUtils.unregisterFactory(ReentrantLock.class);
    }

    @Test
    public void test1() {
//        final ReentrantLock reentrantLock = new ReentrantLock();
        for (int i = 0; i < 10; i++) {
            pool.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        long millis = System.currentTimeMillis();
                        for (int j = 0; j < 1000000; j++) {
                            if (num >= 0) {
//                                reentrantLock.lock();
                                LockUtils.lock(LockUtilsTest.class.getName());
                                try {
//                                synchronized (this) {
                                    if (num >= 0) {
                                        System.out.println(Thread.currentThread().getName() + " | " + (num--));
//                                        Thread.sleep(60);
                                    }
//                                }
                                }
                                finally {
//                                    reentrantLock.unlock();
                                    LockUtils.unlock(LockUtilsTest.class.getName());
                                }
                            }
                        }
                        System.out.println(System.currentTimeMillis() - millis);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        ThreadUtils.sleepQuietly(10000);
    }

}
