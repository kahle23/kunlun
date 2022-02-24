package artoria.cache;

import artoria.exception.ExceptionUtils;

import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * The undefined cache.
 * @author Kahle
 */
public class UndefinedCache extends AbstractCache {

    public UndefinedCache(String name) {

        super(name);
    }

    @Override
    public <T> T get(Object key, Callable<T> callable) {
        try {
            return callable.call();
        }
        catch (Exception e) {
            throw ExceptionUtils.wrap(e);
        }
    }

    @Override
    public Object get(Object key) {

        return null;
    }

    @Override
    public Object put(Object key, Object value) {

        return null;
    }

    @Override
    public Object put(Object key, Object value, long timeToLive, TimeUnit timeUnit) {

        return null;
    }

    @Override
    public Object putIfAbsent(Object key, Object value) {

        return null;
    }

    @Override
    public Object putIfAbsent(Object key, Object value, long timeToLive, TimeUnit timeUnit) {

        return null;
    }

    @Override
    public boolean expire(Object key, long timeToLive, TimeUnit timeUnit) {

        return false;
    }

    @Override
    public boolean expireAt(Object key, Date date) {

        return false;
    }

    @Override
    public boolean persist(Object key) {

        return false;
    }

    @Override
    public Object remove(Object key) {

        return null;
    }

}
