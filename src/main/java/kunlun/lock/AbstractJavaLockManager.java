/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.lock;

import kunlun.exception.ExceptionUtils;
import kunlun.util.Assert;

import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

/**
 * The abstract java lock manager.
 * @see java.util.concurrent.locks.Lock
 * @see kunlun.lock.LockManager
 * @author Kahle
 */
public abstract class AbstractJavaLockManager implements LockManager {
    private final Map<String, Lock> storage;

    protected AbstractJavaLockManager(Map<String, Lock> storage) {
        Assert.notNull(storage, "Parameter \"storage\" must not null. ");
        this.storage = storage;
    }

    /**
     * Create a lock object.
     * @param lockName The lock's name
     * @return The lock object
     */
    protected abstract Lock createLock(String lockName);

    @Override
    public Lock getLock(String lockName) {
        Assert.notBlank(lockName, "Parameter \"lockName\" must not blank. ");
        Lock lock = storage.get(lockName);
        if (lock != null) { return lock; }
        synchronized (this) {
            if ((lock = storage.get(lockName)) != null) {
                return lock;
            }
            storage.put(lockName, lock = createLock(lockName));
        }
        return lock;
    }

    @Override
    public Object removeLock(String lockName) {

        return storage.remove(lockName);
    }

    @Override
    public void lock(String lockName) {

        getLock(lockName).lock();
    }

    @Override
    public void unlock(String lockName) {
        // 20221110 You cannot delete the lock object as soon as you unlock it.
        // 20221109 If the unlocking fails, the lock object should not be deleted.
        getLock(lockName).unlock();
    }

    @Override
    public void lockInterruptibly(String lockName) throws InterruptedException {

        getLock(lockName).lockInterruptibly();
    }

    @Override
    public boolean tryLock(String lockName) {

        return getLock(lockName).tryLock();
    }

    @Override
    public boolean tryLock(String lockName, long time, TimeUnit unit) {
        try {
            return getLock(lockName).tryLock(time, unit);
        }
        catch (InterruptedException e) {
            throw ExceptionUtils.wrap(e);
        }
    }

}
