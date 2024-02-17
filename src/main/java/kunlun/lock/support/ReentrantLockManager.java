/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.lock.support;

import kunlun.lock.AbstractJavaLockManager;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * The reentrant lock manager.
 * @see java.util.concurrent.locks.ReentrantLock
 * @author Kahle
 */
public class ReentrantLockManager extends AbstractJavaLockManager {

    public ReentrantLockManager() {
        // When using ReentrantLock, it is usually a static variable, so ConcurrentHashMap is OK.
        super(new ConcurrentHashMap<String, Lock>());
    }

    public ReentrantLockManager(Map<String, Lock> storage) {

        super(storage);
    }

    @Override
    protected Lock createLock(String lockName) {

        return new ReentrantLock();
    }

}
