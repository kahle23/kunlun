package artoria.lock;

import artoria.util.Assert;

import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * Lock tools.
 * @author Kahle
 */
public class LockUtils {
    private static Logger log = Logger.getLogger(LockUtils.class.getName());
    private static Locker locker;

    public static Locker getLocker() {
        if (locker != null) {
            return locker;
        }
        synchronized (Locker.class) {
            if (locker != null) {
                return locker;
            }
            setLocker(new ReentrantLocker());
            return locker;
        }
    }

    public static void setLocker(Locker locker) {
        Assert.notNull(locker, "Parameter \"locker\" must not null. ");
        synchronized (Locker.class) {
            LockUtils.locker = locker;
            log.info("Set locker: " + locker.getClass().getName());
        }
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
