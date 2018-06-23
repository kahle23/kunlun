package artoria.lock;

import artoria.collection.ReferenceHashMap;
import artoria.util.Assert;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Reentrant lock operator.
 * @see java.util.concurrent.locks.ReentrantLock
 * @see artoria.lock.Locker
 * @author Kahle
 */
public class ReentrantLocker implements Locker {
    private final ReentrantLock PRODUCT_LOCK = new ReentrantLock();
    private Map<String, Lock> storage;

    private Lock takeLock(String lockName) {
        Lock lock = storage.get(lockName);
        if (lock != null) { return lock; }
        try {
            PRODUCT_LOCK.lock();
            lock = storage.get(lockName);
            if (lock != null) { return lock; }
            lock = new ReentrantLock();
            storage.put(lockName, lock);
            return lock;
        }
        finally {
            PRODUCT_LOCK.unlock();
        }
    }

    public ReentrantLocker() {
        ReferenceHashMap.Type type = ReferenceHashMap.Type.SOFT;
        Map<String, Lock> map = new ReferenceHashMap<String, Lock>(type);
        this.storage = Collections.synchronizedMap(map);
    }

    public ReentrantLocker(Map<String, Lock> storage) {
        Assert.notNull(storage, "Parameter \"storage\" must not null. ");
        this.storage = storage;
    }

    @Override
    public void lock(String lockName) {
        this.takeLock(lockName).lock();
    }

    @Override
    public void unlock(String lockName) {
        this.takeLock(lockName).unlock();
    }

    @Override
    public void lockInterruptibly(String lockName) throws InterruptedException {
        this.takeLock(lockName).lockInterruptibly();
    }

    @Override
    public boolean tryLock(String lockName) {
        return this.takeLock(lockName).tryLock();
    }

    @Override
    public boolean tryLock(String lockName, long time, TimeUnit unit) throws InterruptedException {
        return this.takeLock(lockName).tryLock(time, unit);
    }

}
