package artoria.lifecycle;

import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.util.CollectionUtils;

import java.util.Collection;

/**
 * Help call {@link Initializable#initialize()} or {@link Destroyable#destroy()}.
 * @author Kahle
 */
public class LifecycleUtils {
    private static Logger log = LoggerFactory.getLogger(LifecycleUtils.class);

    public static void initialize(Object obj) throws LifecycleException {
        if (obj instanceof Initializable) {
            LifecycleUtils.initialize((Initializable) obj);
        }
        else if (obj instanceof Collection) {
            LifecycleUtils.initialize((Collection) obj);
        }
    }

    public static void initialize(Collection<?> collection) throws LifecycleException {
        if (CollectionUtils.isEmpty(collection)) {
            return;
        }
        for (Object obj : collection) {
            LifecycleUtils.initialize(obj);
        }
    }

    public static void initialize(Initializable initializable) throws LifecycleException {
        if (initializable == null) {
            return;
        }
        initializable.initialize();
    }

    public static void destroy(Object obj) {
        if (obj instanceof Destroyable) {
            LifecycleUtils.destroy((Destroyable) obj);
        }
        else if (obj instanceof Collection) {
            LifecycleUtils.destroy((Collection) obj);
        }
    }

    public static void destroy(Collection<?> collection) {
        if (CollectionUtils.isEmpty(collection)) {
            return;
        }
        for (Object obj : collection) {
            LifecycleUtils.destroy(obj);
        }
    }

    public static void destroy(Destroyable destroyable) {
        if (destroyable == null) {
            return;
        }
        try {
            destroyable.destroy();
        }
        catch (Throwable t) {
            log.info("Unable to cleanly destroy instance [{}] of type [{}]. "
                    , destroyable, destroyable.getClass().getName());
        }
    }

}
