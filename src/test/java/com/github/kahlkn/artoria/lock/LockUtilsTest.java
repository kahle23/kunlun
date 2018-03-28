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
    }

    @After
    public void destroy() {
        pool.shutdown();
    }

    @Test
    public void testRegister() {
        SimpleLockFactory lockFactory = new SimpleLockFactory();
        LockUtils.register(ReentrantLock.class, lockFactory);
    }

    @Test
    public void testUnregister() {
        LockUtils.unregister(ReentrantLock.class);
    }

    @Test
    public void test1() {
        for (int i = 0; i < 10; i++) {
            pool.submit(new Runnable() {
                @Override
                public void run() {
                    for (int j = 0; j < 1000000; j++) {
                        if (num >= 0) {
                            LockUtils.lock(LockUtilsTest.class.getName());
                            try {
                                if (num >= 0) {
                                    System.out.println(Thread.currentThread().getName() + " | " + (num--));
                                }
                            }
                            finally {
                                LockUtils.unlock(LockUtilsTest.class.getName());
                            }
                        }
                    }
                }
            });
        }
        ThreadUtils.sleepQuietly(10000);
    }
}
