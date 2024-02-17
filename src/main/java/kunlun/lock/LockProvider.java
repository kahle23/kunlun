/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.lock;

import java.util.concurrent.TimeUnit;

/**
 * The lock provider.
 * It is not necessary to unpack the manager name based on the lock name.
 * The caller should explicitly specify the lock manager name.
 * If no manager name is specified, the default lock manager is used.
 * @author Kahle
 */
public interface LockProvider {

    /**
     * Register the lock manager.
     * @param managerName The lock manager name
     * @param lockManager The property source
     */
    void registerManager(String managerName, LockManager lockManager);

    /**
     * Deregister the lock manager.
     * @param managerName The lock manager name
     */
    void deregisterManager(String managerName);

    /**
     * Get the lock manager.
     * @param managerName The lock manager name
     * @return The lock manager
     */
    LockManager getLockManager(String managerName);

    /**
     * Get the lock object.
     * @param managerName The lock manager name
     * @param lockName The lock's name
     * @return The lock object
     */
    Object getLock(String managerName, String lockName);

    /**
     * Remove the lock object.
     * This method will be necessary if the lock name
     *   is generated from something like an order number.
     * @param managerName The lock manager name
     * @param lockName The lock's name
     * @return The lock object or null
     */
    Object removeLock(String managerName, String lockName);

    /**
     * Acquires the lock.
     * @param managerName The lock manager name
     * @param lockName The lock's name
     */
    void lock(String managerName, String lockName);

    /**
     * Releases the lock.
     * @param managerName The lock manager name
     * @param lockName The lock's name
     */
    void unlock(String managerName, String lockName);

    /**
     * Acquires the lock unless the current thread is interrupted.
     * @param managerName The lock manager name
     * @param lockName The lock's name
     * @throws InterruptedException This means that the current thread is interrupted
     */
    void lockInterruptibly(String managerName, String lockName) throws InterruptedException;

    /**
     * Acquires the lock only if it is free at the time of invocation.
     * @param managerName The lock manager name
     * @param lockName The lock's name
     * @return True if the lock was acquired and false otherwise
     */
    boolean tryLock(String managerName, String lockName);

    /**
     * Acquires the lock if it is free within the given waiting time and the
     *  current thread has not been interrupted (interrupt exception may be nested).
     * @param managerName The lock manager name
     * @param lockName The lock's name
     * @param time The maximum time to wait for the lock
     * @param unit The time unit of the time argument
     * @return True if the lock was acquired and false otherwise
     */
    boolean tryLock(String managerName, String lockName, long time, TimeUnit unit);

}
