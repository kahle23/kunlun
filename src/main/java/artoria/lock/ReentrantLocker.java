package artoria.lock;

import artoria.collection.ReferenceMap;
import artoria.util.Assert;

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

    public ReentrantLocker() {
        ReferenceMap.Type type = ReferenceMap.Type.SOFT;
        this.storage = new ReferenceMap<String, Lock>(type, true);
    }

    public ReentrantLocker(Map<String, Lock> storage) {
        Assert.notNull(storage, "Parameter \"storage\" must not null. ");
        this.storage = storage;
    }

    private Lock getLock(String lockName) {
        Assert.notBlank(lockName, "Parameter \"lockName\" must not blank. ");
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

    @Override
    public void lock(String lockName) {

        this.getLock(lockName).lock();
    }

    @Override
    public void unlock(String lockName) {

        this.getLock(lockName).unlock();
    }

    @Override
    public void lockInterruptibly(String lockName) throws InterruptedException {

        this.getLock(lockName).lockInterruptibly();
    }

    @Override
    public boolean tryLock(String lockName) {

        return this.getLock(lockName).tryLock();
    }

    @Override
    public boolean tryLock(String lockName, long time, TimeUnit unit) throws InterruptedException {

        return this.getLock(lockName).tryLock(time, unit);
    }

}
