package artoria.lock;

import java.util.concurrent.TimeUnit;

/**
 * Locker to use operate lock.
 * @see java.util.concurrent.locks.Lock
 * @author Kahle
 */
public interface Locker {

    /**
     * Acquires the lock.
     * @param lockName Lock's name
     */
    void lock(String lockName);

    /**
     * Releases the lock.
     * @param lockName Lock's name
     */
    void unlock(String lockName);

    /**
     * Acquires the lock unless the current thread is
     * {@linkplain Thread#interrupt interrupted}.
     * @param lockName Lock's name
     * @throws InterruptedException if the current thread is
     *         interrupted while acquiring the lock (and interruption
     *         of lock acquisition is supported)
     */
    void lockInterruptibly(String lockName) throws InterruptedException;

    /**
     * Acquires the lock only if it is free at the time of invocation.
     * @param lockName Lock's name
     * @return True is the lock was acquired, false is not
     */
    boolean tryLock(String lockName);

    /**
     * Acquires the lock if it is free within the given waiting time and the
     * current thread has not been {@linkplain Thread#interrupt interrupted}.
     * @param lockName Lock's name
     * @param time The maximum time to wait for the lock
     * @param unit The time unit of the {@code time} argument
     * @return True is the lock was acquired, false is not
     * @throws InterruptedException if the current thread is interrupted
     *         while acquiring the lock (and interruption of lock
     *         acquisition is supported)
     */
    boolean tryLock(String lockName, long time, TimeUnit unit) throws InterruptedException;

}
