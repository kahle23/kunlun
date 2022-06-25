package artoria.lock;

import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.util.Assert;

import java.util.concurrent.TimeUnit;

import static artoria.common.Constants.EMPTY_STRING;

/**
 * The lock tools.
 * @author Kahle
 */
public class LockUtils {
    private static Logger log = LoggerFactory.getLogger(LockUtils.class);
    private static volatile LockProvider lockProvider;

    public static LockProvider getLockProvider() {
        if (lockProvider != null) { return lockProvider; }
        synchronized (LockUtils.class) {
            if (lockProvider != null) { return lockProvider; }
            LockUtils.setLockProvider(new SimpleLockProvider());
            return lockProvider;
        }
    }

    public static void setLockProvider(LockProvider lockProvider) {
        Assert.notNull(lockProvider, "Parameter \"lockProvider\" must not null. ");
        log.info("Set lock provider: {}", lockProvider.getClass().getName());
        LockUtils.lockProvider = lockProvider;
    }

    public static void registerManager(String managerName, LockManager lockManager) {

        getLockProvider().registerManager(managerName, lockManager);
    }

    public static void deregisterManager(String managerName) {

        getLockProvider().deregisterManager(managerName);
    }

    public static LockManager getLockManager(String managerName) {

        return getLockProvider().getLockManager(managerName);
    }

    public static Object getLock(String lockName) {

        return getLock(EMPTY_STRING, lockName);
    }

    public static Object removeLock(String lockName) {

        return removeLock(EMPTY_STRING, lockName);
    }

    public static void lock(String lockName) {

        lock(EMPTY_STRING, lockName);
    }

    public static void unlock(String lockName) {

        unlock(EMPTY_STRING, lockName);
    }

    public static void lockInterruptibly(String lockName) throws InterruptedException {

        lockInterruptibly(EMPTY_STRING, lockName);
    }

    public static boolean tryLock(String lockName) {

        return tryLock(EMPTY_STRING, lockName);
    }

    public static boolean tryLock(String lockName, long time, TimeUnit unit) {

        return tryLock(EMPTY_STRING, lockName, time, unit);
    }

    public static Object getLock(String managerName, String lockName) {

        return getLockProvider().getLock(managerName, lockName);
    }

    public static Object removeLock(String managerName, String lockName) {

        return getLockProvider().removeLock(managerName, lockName);
    }

    public static void lock(String managerName, String lockName) {

        getLockProvider().lock(managerName, lockName);
    }

    public static void unlock(String managerName, String lockName) {

        getLockProvider().unlock(managerName, lockName);
    }

    public static void lockInterruptibly(String managerName, String lockName) throws InterruptedException {

        getLockProvider().lockInterruptibly(managerName, lockName);
    }

    public static boolean tryLock(String managerName, String lockName) {

        return getLockProvider().tryLock(managerName, lockName);
    }

    public static boolean tryLock(String managerName, String lockName, long time, TimeUnit unit) {

        return getLockProvider().tryLock(managerName, lockName, time, unit);
    }

}
