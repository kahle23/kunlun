package artoria.bean;

import artoria.bean.support.SimpleBeanManager;
import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.util.Assert;

import java.util.Map;

/**
 * The static holder tools for bean instance.
 * @author Kahle
 */
public class BeanHolder {
    private static Logger log = LoggerFactory.getLogger(BeanHolder.class);
    private static volatile BeanManager beanManager;

    public static BeanManager getBeanManager() {
        if (beanManager != null) { return beanManager; }
        synchronized (BeanHolder.class) {
            if (beanManager != null) { return beanManager; }
            BeanHolder.setBeanManager(new SimpleBeanManager());
            return beanManager;
        }
    }

    public static void setBeanManager(BeanManager beanManager) {
        Assert.notNull(beanManager, "Parameter \"beanManager\" must not null. ");
        log.info("Set bean manager: {}", beanManager.getClass().getName());
        BeanHolder.beanManager = beanManager;
    }

    public static boolean contains(String name) {

        return getBeanManager().contains(name);
    }

    public static Object remove(String name) {

        return getBeanManager().remove(name);
    }

    public static Object get(String name) {

        return getBeanManager().get(name);
    }

    public static <T> T get(Class<T> type) {

        return getBeanManager().get(type);
    }

    public static <T> T get(String name, Class<T> type) {

        return getBeanManager().get(name, type);
    }

    public static Object put(String name, Object bean) {

        return getBeanManager().put(name, bean);
    }

    public static String[] getAliases(String name) {

        return getBeanManager().getAliases(name);
    }

    public static String[] getNames(Class<?> type) {

        return getBeanManager().getNames(type);
    }

    public static <T> Map<String, T> getBeans(Class<T> type) {

        return getBeanManager().getBeans(type);
    }

}
