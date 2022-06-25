package artoria.lock;

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
