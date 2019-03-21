package artoria.lock;

import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.util.ThreadUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;

public class LockUtilsTest {
    private static Logger log = LoggerFactory.getLogger(LockUtilsTest.class);
    private ExecutorService pool;
    private Integer num = 100;

    @Before
    public void init() {
        ThreadFactory threadFactory = Executors.defaultThreadFactory();
        pool = new ThreadPoolExecutor(20, 20, 0L
                , TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(), threadFactory);
    }

    @After
    public void destroy() {

        pool.shutdown();
    }

    @Test
    public void test1() {
        final String lockName = "test1";
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                String threadName = Thread.currentThread().getName();
                long millis = System.currentTimeMillis();
                for (int j = 0; j < 1000000; j++) {
                    if (num < 0) { continue; }
                    try {
                        log.info(threadName);
                        LockUtils.lock(lockName);
                        if (num < 0) { continue; }
                        log.info("{} | {}", threadName, num--);
                    }
                    finally {
                        log.info("{} unlock", threadName);
                        LockUtils.unlock(lockName);
                    }
                }
                log.info("{}: {}ms", threadName, System.currentTimeMillis() - millis);
            }
        };
        for (int i = 0; i < 20; i++) {
            pool.submit(runnable);
        }
        ThreadUtils.sleepQuietly(1000);
    }

    @Test
    public void test2() {
        final String lockName = "test2";
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                String threadName = Thread.currentThread().getName();
                long millis = System.currentTimeMillis();
                for (int j = 0; j < 1000000; j++) {
                    if (num < 0) { continue; }
                    // boolean tryLock = LockUtils.tryLock(lockName);
                    boolean tryLock = false;
                    try {
                        tryLock = LockUtils.tryLock(lockName, 50, TimeUnit.MILLISECONDS);
                    }
                    catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    log.info("{} tryLock: {}", threadName, tryLock);
                    if (!tryLock) { continue; }
                    try {
                        if (num < 0) { continue; }
                        log.info("{} | {}", threadName, num--);
                    }
                    finally {
                        log.info("{} unlock", threadName);
                        LockUtils.unlock(lockName);
                    }
                }
                log.info("{}: {}ms", threadName, System.currentTimeMillis() - millis);
            }
        };
        for (int i = 0; i < 20; i++) {
            pool.submit(runnable);
        }
        ThreadUtils.sleepQuietly(1000);
    }

    @Test
    @Ignore
    public void test3() {
        final ReentrantLock lock = new ReentrantLock();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                String threadName = Thread.currentThread().getName();
                long millis = System.currentTimeMillis();
                for (int j = 0; j < 1000000; j++) {
                    if (num < 0) { continue; }
                    try {
                        log.info(threadName);
                        lock.lock();
                        if (num < 0) { continue; }
                        log.info("{} | {}", threadName, num--);
                    }
                    finally {
                        log.info("{} unlock", threadName);
                        lock.unlock();
                    }
                }
                log.info("{}: {}ms", threadName, System.currentTimeMillis() - millis);
            }
        };
        for (int i = 0; i < 20; i++) {
            pool.submit(runnable);
        }
        ThreadUtils.sleepQuietly(1000);
    }

    @Test
    @Ignore
    public void test4() {
        final ReentrantLock lock = new ReentrantLock();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                String threadName = Thread.currentThread().getName();
                long millis = System.currentTimeMillis();
                for (int j = 0; j < 1000000; j++) {
                    if (num < 0) { continue; }
                    // boolean tryLock = lock.tryLock();
                    boolean tryLock = false;
                    try {
                        tryLock = lock.tryLock(50, TimeUnit.MILLISECONDS);
                    }
                    catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    log.info("{} tryLock: {}", threadName, tryLock);
                    if (!tryLock) { continue; }
                    try {
                        if (num < 0) { continue; }
                        log.info("{} | {}", threadName, num--);
                    }
                    finally {
                        log.info("{} unlock", threadName);
                        lock.unlock();
                    }
                }
                log.info("{}: {}ms", threadName, System.currentTimeMillis() - millis);
            }
        };
        for (int i = 0; i < 20; i++) {
            pool.submit(runnable);
        }
        ThreadUtils.sleepQuietly(1000);
    }

    @Test
    @Ignore
    public void test5() {
        final Object lock = new Object();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                String threadName = Thread.currentThread().getName();
                long millis = System.currentTimeMillis();
                for (int j = 0; j < 1000000; j++) {
                    if (num < 0) { continue; }
                    log.info(threadName);
                    synchronized (lock) {
                        if (num >= 0) {
                            log.info("{} | {}", threadName, num--);
                        }
                        log.info("{} unlock", threadName);
                    }
                }
                log.info("{}: {}ms", threadName, System.currentTimeMillis() - millis);
            }
        };
        for (int i = 0; i < 20; i++) {
            pool.submit(runnable);
        }
        ThreadUtils.sleepQuietly(1000);
    }

}
