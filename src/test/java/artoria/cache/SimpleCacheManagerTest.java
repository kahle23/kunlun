package artoria.cache;

import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import org.junit.Ignore;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class SimpleCacheManagerTest {
    private static Logger log = LoggerFactory.getLogger(SimpleCacheManagerTest.class);
    private static CacheManager cacheManager = new SimpleCacheManager();
    private static final String CACHE_NAME = "TEST";

    static {
        SimpleConfiguration<String, Object> config = new SimpleConfiguration<String, Object>(String.class, Object.class);
        config.setCacheLoader(new CacheLoader<String, Object>() {
            @Override
            public Object load(String key) throws CacheException {
                return key + " - load - data";
            }
            @Override
            public Map<String, Object> loadAll(Iterable<? extends String> keys) throws CacheException {
                Map<String, Object> result = new HashMap<String, Object>();
                for (String key : keys) {
                    result.put(key, this.load(key));
                }
                return result;
            }
        });
        cacheManager.createCache(CACHE_NAME, config);
    }

    @Test
    public void test1() {
        Cache<String, Object> cache = cacheManager.getCache(CACHE_NAME);
        for (int i = 0; i < 100; i++) {
            log.info("" + cache.get("" + i));
        }
    }

    @Test
    public void test2() {
        Cache<String, Object> cache = cacheManager.getCache(CACHE_NAME);
        for (int i = 0; i < 100; i++) {
            cache.put("" + i, "test2 - data - " + i);
        }
        for (int i = 0; i < 100; i++) {
            log.info("" + cache.get("" + i));
        }
    }

    @Ignore
    @Test
    public void test3() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 1000; i++) {
            builder.append("data - ").append(i).append(" | ");
        }
        Cache<String, Object> cache = cacheManager.getCache(CACHE_NAME);
        for (int i = 0; i <= 9999999; i++) {
            cache.put("key" + i, builder.toString());
            log.info("" + cache.size());
        }
    }

}
