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
    private static Logger log = LoggerFactory.getLogger(LockUtils.class);
    private static final Map<Class<? extends Lock>, LockFactory> FACTORYS;
    private static final Map<String, Lock> LOCKS;

    static {
        FACTORYS = new ConcurrentHashMap<Class<? extends Lock>, LockFactory>();
        LOCKS = new ConcurrentHashMap<String, Lock>();
        LockUtils.registerFactory(ReentrantLock.class, new SimpleLockFactory());
    }

    private static Lock getLock(String lockName) {
        Lock lock = LOCKS.get(lockName);
        if (lock == null) {
            throw new LockException("Can not find the \""
                    + lockName + "\" corresponding to lock, please register first. ");
        }
        return lock;
    }

    private static LockFactory getLockFactory(Class<? extends Lock> lockClass) {
        LockFactory lockFactory = FACTORYS.get(lockClass);
        if (lockFactory == null) {
            throw new LockException("Can not find the \""
                    + lockClass.getName() + "\" corresponding to LockFactory, please register first. ");
        }
        return lockFactory;
    }

    public static void registerFactory(Class<? extends Lock> lockClass, LockFactory lockFactory) {
        Assert.notNull(lockClass, "Parameter \"lockClass\" must not null. ");
        Assert.notNull(lockFactory, "Parameter \"lockFactory\" must not null. ");
        FACTORYS.put(lockClass, lockFactory);
        String lockName = lockClass.getName();
        String lockFactoryName = lockFactory.getClass().getName();
        log.info("Register lock factory: " + lockName + " >> " + lockFactoryName);
    }

    public static void unregisterFactory(Class<? extends Lock> lockClass) {
        Assert.notNull(lockClass, "Parameter \"lockClass\" must not null. ");
        LockFactory lockFactory = FACTORYS.remove(lockClass);
        if (lockFactory == null) { return; }
        String lockName = lockClass.getName();
        String lockFactoryName = lockFactory.getClass().getName();
        log.info("Unregister lock factory: " + lockName + " >> " + lockFactoryName);
    }

    public static void registerLock(String lockName, Lock lock) {
        Assert.notBlank(lockName, "Parameter \"lockName\" must not blank. ");
        Assert.notNull(lock, "Parameter \"lock\" must not null. ");
        Lock oldLock = null; boolean hasOldLock = false;
        // TODO: when jdk 1.8 + , using putIfAbsent optimize
        if (LOCKS.containsKey(lockName) ||
                (hasOldLock = ((oldLock = LOCKS.put(lockName, lock)) != null))) {
            if (hasOldLock) { LOCKS.put(lockName, oldLock); }
            throw new LockException("This lock name already mapping a lock. ");
        }
    }

    public static void registerLock(String lockName, Class<? extends Lock> lockClass) {
        Assert.notNull(lockClass, "Parameter \"lockClass\" must not null. ");
        LockFactory lockFactory = LockUtils.getLockFactory(lockClass);
        LockUtils.registerLock(lockName, lockFactory.getInstance(lockName, lockClass));
    }

    public static void unregisterLock(String lockName) {
        Assert.notBlank(lockName, "Parameter \"lockName\" must not blank. ");
        LOCKS.remove(lockName);
    }

    public static void lock(String lockName) {
        Assert.notBlank(lockName, "Parameter \"lockName\" must not blank. ");
        Lock lock = LockUtils.getLock(lockName);
        lock.lock();
    }

    public static void unlock(String lockName) {
        Assert.notBlank(lockName, "Parameter \"lockName\" must not blank. ");
        Lock lock = LockUtils.getLock(lockName);
        lock.unlock();
    }

}
