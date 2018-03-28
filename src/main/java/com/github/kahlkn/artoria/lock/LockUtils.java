package com.github.kahlkn.artoria.lock;

import com.github.kahlkn.artoria.logging.Logger;
import com.github.kahlkn.artoria.logging.LoggerFactory;
import com.github.kahlkn.artoria.util.Assert;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Lock tools.
 * @author Kahle
 */
public class LockUtils {
    private static final Map<Class<? extends Lock>, LockFactory> FACTORYS;
    private static final Map<String, Lock> LOCKS;
    private static Class<? extends Lock> defaultLockClass;
    private static Logger log = LoggerFactory.getLogger(LockUtils.class);

    static {
        FACTORYS = new ConcurrentHashMap<Class<? extends Lock>, LockFactory>();
        LOCKS = new ConcurrentHashMap<String, Lock>();
        Class<ReentrantLock> lockClass = ReentrantLock.class;
        SimpleLockFactory lockFactory = new SimpleLockFactory();
        LockUtils.setDefaultLockClass(lockClass);
        LockUtils.register(lockClass, lockFactory);
    }

    public static Class<? extends Lock> getDefaultLockClass() {
        return defaultLockClass;
    }

    public static void setDefaultLockClass(Class<? extends Lock> defaultLockClass) {
        Assert.notNull(defaultLockClass, "Parameter \"defaultLockClass\" must not null. ");
        LockUtils.defaultLockClass = defaultLockClass;
        log.info("Set default lock class: " + defaultLockClass.getName());
    }

    public static void register(Class<? extends Lock> lockClass, LockFactory lockFactory) {
        Assert.notNull(lockClass, "Parameter \"lockClass\" must not null. ");
        Assert.notNull(lockFactory, "Parameter \"lockFactory\" must not null. ");
        FACTORYS.put(lockClass, lockFactory);
        log.info("Register lock factory: "
                + lockClass.getName() + " >> " + lockFactory.getClass().getName());
    }

    public static void unregister(Class<? extends Lock> lockClass) {
        Assert.notNull(lockClass, "Parameter \"lockClass\" must not null. ");
        LockFactory lockFactory = FACTORYS.remove(lockClass);
        if (lockFactory != null) {
            log.info("Unregister lock factory: "
                    + lockClass.getName() + " >> " + lockFactory.getClass().getName());
        }
    }

    public static void lock(String lockName) {
        LockUtils.lock(lockName, defaultLockClass);
    }

    public static void lock(String lockName, Class<? extends Lock> lockClass) {
        Lock lock = LOCKS.get(lockName);
        if (lock == null) {
            LockFactory lockFactory = FACTORYS.get(lockClass);
            Assert.notNull(lockFactory, "Can not find the \""
                    + lockClass.getName() + "\" corresponding to LockFactory, please register first. ");
            lock = lockFactory.getInstance(lockName, lockClass);
            // TODO: when jdk 1.8 +
            // Lock putIfAbsent = LOCKS.putIfAbsent(lockName, lock);
            // if (putIfAbsent != null) { lock = putIfAbsent; }
            Lock oldLock = LOCKS.put(lockName, lock);
            if (oldLock != null) {
                LOCKS.put(lockName, oldLock);
                lock = oldLock;
            }
        }
        lock.lock();
    }

    public static void unlock(String lockName) {
        Lock lock = LOCKS.get(lockName);
        if (lock == null) { return; }
        lock.unlock();
    }

    public static void remove(String lockName) {
        LOCKS.remove(lockName);
    }

    public static void clear() {
        LOCKS.clear();
    }

}
