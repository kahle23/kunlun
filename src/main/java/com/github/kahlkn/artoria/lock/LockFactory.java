package com.github.kahlkn.artoria.lock;

import java.util.concurrent.locks.Lock;

/**
 * Lock factory.
 * @author Kahle
 */
public interface LockFactory {

    /**
     * Get lock object by name and class.
     * @param lockName Lock name
     * @param lockClass Lock class
     * @return Lock object
     */
    Lock getInstance(String lockName, Class<? extends Lock> lockClass);

}
