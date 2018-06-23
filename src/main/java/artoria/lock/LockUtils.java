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

    static {
        LockUtils.setLocker(new ReentrantLocker());
    }

    public static Locker getLocker() {
        return locker;
    }

    public static void setLocker(Locker locker) {
        Assert.notNull(locker, "Parameter \"locker\" must not null. ");
        LockUtils.locker = locker;
        log.info("Set locker: " + locker.getClass().getName());
    }

    public static void lock(String lockName) {
        locker.lock(lockName);
    }

    public static void unlock(String lockName) {
        locker.unlock(lockName);
    }

    public void lockInterruptibly(String lockName) throws InterruptedException {
        locker.lockInterruptibly(lockName);
    }

    public static boolean tryLock(String lockName) {
        return locker.tryLock(lockName);
    }

    public static boolean tryLock(String lockName, long time, TimeUnit unit) throws InterruptedException {
        return locker.tryLock(lockName, time, unit);
    }

}
