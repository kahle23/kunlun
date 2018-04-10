package com.github.kahlkn.artoria.lock;

import com.github.kahlkn.artoria.exception.ExceptionUtils;

import java.util.concurrent.locks.Lock;

/**
 * Simple implement lock factory.
 * @author Kahle
 */
public class SimpleLockFactory implements LockFactory {

    @Override
    public Lock getInstance(String lockName, Class<? extends Lock> lockClass) {
        try {
            return lockClass.newInstance();
        }
        catch (Exception e) {
            throw ExceptionUtils.wrap(e);
        }
    }

}
