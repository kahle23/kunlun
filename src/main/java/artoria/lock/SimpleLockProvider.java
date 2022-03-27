package artoria.lock;

import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.util.Assert;
import artoria.util.StringUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * The simple lock provider.
 * @author Kahle
 */
public class SimpleLockProvider implements LockProvider {
    private static Logger log = LoggerFactory.getLogger(SimpleLockProvider.class);
    protected final Map<String, LockManager> lockManagers;
    private final String defaultManagerName;

    public SimpleLockProvider() {

        this("local");
    }

    public SimpleLockProvider(String defaultManagerName) {

        this(new ConcurrentHashMap<String, LockManager>(), defaultManagerName);
    }

    protected SimpleLockProvider(Map<String, LockManager> lockManagers,
                                 String defaultManagerName) {
        Assert.notBlank(defaultManagerName, "Parameter \"defaultManagerName\" must not blank. ");
        Assert.notNull(lockManagers, "Parameter \"lockManagers\" must not null. ");
        this.defaultManagerName = defaultManagerName;
        this.lockManagers = lockManagers;
        registerManager(defaultManagerName, new ReentrantLockManager());
    }

    @Override
    public void registerManager(String managerName, LockManager lockManager) {
        Assert.notBlank(managerName, "Parameter \"managerName\" must not blank. ");
        Assert.notNull(lockManager, "Parameter \"lockManager\" must not null. ");
        String className = lockManager.getClass().getName();
        lockManagers.put(managerName, lockManager);
        log.info("Register the lock manager \"{}\" to \"{}\". ", className, managerName);
    }

    @Override
    public void deregisterManager(String managerName) {
        Assert.notBlank(managerName, "Parameter \"managerName\" must not blank. ");
        LockManager remove = lockManagers.remove(managerName);
        if (remove != null) {
            String className = remove.getClass().getName();
            log.info("Deregister the lock manager \"{}\" from \"{}\". ", className, managerName);
        }
    }

    @Override
    public LockManager getLockManager(String managerName) {
        if (StringUtils.isBlank(managerName)) { managerName = defaultManagerName; }
        LockManager lockManager = lockManagers.get(managerName);
        Assert.state(lockManager != null, "The lock manager does not exist. ");
        return lockManager;
    }

    @Override
    public Object getLock(String managerName, String lockName) {

        return getLockManager(managerName).getLock(lockName);
    }

    @Override
    public Object removeLock(String managerName, String lockName) {

        return getLockManager(managerName).removeLock(lockName);
    }

    @Override
    public void lock(String managerName, String lockName) {

        getLockManager(managerName).lock(lockName);
    }

    @Override
    public void unlock(String managerName, String lockName) {

        getLockManager(managerName).unlock(lockName);
    }

    @Override
    public void lockInterruptibly(String managerName, String lockName) throws InterruptedException {

        getLockManager(managerName).lockInterruptibly(lockName);
    }

    @Override
    public boolean tryLock(String managerName, String lockName) {

        return getLockManager(managerName).tryLock(lockName);
    }

    @Override
    public boolean tryLock(String managerName, String lockName, long time, TimeUnit unit) {

        return getLockManager(managerName).tryLock(lockName, time, unit);
    }

}
