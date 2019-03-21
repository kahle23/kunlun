package artoria.lock;

import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.util.Assert;

import java.util.concurrent.TimeUnit;

/**
 * Lock tools.
 * @author Kahle
 */
public class LockUtils {
    private static final Locker DEFAULT_LOCKER = new ReentrantLocker();
    private static Logger log = LoggerFactory.getLogger(LockUtils.class);
    private static Locker locker;

    public static Locker getLocker() {

        return locker != null ? locker : DEFAULT_LOCKER;
    }

    public static void setLocker(Locker locker) {
        Assert.notNull(locker, "Parameter \"locker\" must not null. ");
        log.info("Set locker: {}", locker.getClass().getName());
        LockUtils.locker = locker;
    }

    public static void lock(String lockName) {

        getLocker().lock(lockName);
    }

    public static void unlock(String lockName) {

        getLocker().unlock(lockName);
    }

    public static void lockInterruptibly(String lockName) throws InterruptedException {

        getLocker().lockInterruptibly(lockName);
    }

    public static boolean tryLock(String lockName) {

        return getLocker().tryLock(lockName);
    }

    public static boolean tryLock(String lockName, long time, TimeUnit unit) throws InterruptedException {

        return getLocker().tryLock(lockName, time, unit);
    }

}
