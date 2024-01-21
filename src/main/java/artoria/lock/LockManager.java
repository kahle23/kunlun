package artoria.lock;

import java.util.concurrent.TimeUnit;

/**
 * The lock manager.
 * @author Kahle
 */
public interface LockManager {

    /**
     * Get the lock object.
     * @param lockName The lock's name
     * @return The lock object
     */
    Object getLock(String lockName);

    /**
     * Remove the lock object.
     * This method will be necessary if the lock name
     *   is generated from something like an order number.
     * @param lockName The lock's name
     * @return The lock object or null
     */
    Object removeLock(String lockName);

    /**
     * Acquires the lock.
     * @param lockName The lock's name
     */
    void lock(String lockName);

    /**
     * Releases the lock.
     * @param lockName The lock's name
     */
    void unlock(String lockName);

    /**
     * Acquires the lock unless the current thread is interrupted.
     * @param lockName The lock's name
     * @throws InterruptedException This means that the current thread is interrupted
     */
    void lockInterruptibly(String lockName) throws InterruptedException;

    /**
     * Acquires the lock only if it is free at the time of invocation.
     * @param lockName The lock's name
     * @return True if the lock was acquired and false otherwise
     */
    boolean tryLock(String lockName);

    /**
     * Acquires the lock if it is free within the given waiting time and the
     *  current thread has not been interrupted (interrupt exception may be nested).
     * @param lockName The lock's name
     * @param time The maximum time to wait for the lock
     * @param unit The time unit of the time argument
     * @return True if the lock was acquired and false otherwise
     */
    boolean tryLock(String lockName, long time, TimeUnit unit);

}
