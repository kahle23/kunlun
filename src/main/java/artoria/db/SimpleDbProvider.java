package artoria.db;

import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.util.Assert;
import artoria.util.MapUtils;
import artoria.util.ObjectUtils;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The simple database provider.
 * @author Kahle
 */
public class SimpleDbProvider implements DbProvider {
    private static final Logger log = LoggerFactory.getLogger(SimpleDbProvider.class);
    protected final Map<String, DbHandler> handlers;
    protected final Map<String, Object> commonProperties;

    protected SimpleDbProvider(Map<String, Object> commonProperties,
                               Map<String, DbHandler> handlers) {
        Assert.notNull(commonProperties, "Parameter \"commonProperties\" must not null. ");
        Assert.notNull(handlers, "Parameter \"handlers\" must not null. ");
        this.commonProperties = commonProperties;
        this.handlers = handlers;
    }

    public SimpleDbProvider() {
        this(new ConcurrentHashMap<String, Object>(),
                new ConcurrentHashMap<String, DbHandler>());
    }

    @Override
    public void registerCommonProperties(Map<?, ?> commonProperties) {
        if (MapUtils.isEmpty(commonProperties)) { return; }
        for (Map.Entry<?, ?> entry : commonProperties.entrySet()) {
            String keyStr = String.valueOf(entry.getKey());
            this.commonProperties.put(keyStr, entry.getValue());
        }
    }

    @Override
    public void clearCommonProperties() {

        this.commonProperties.clear();
    }

    @Override
    public Map<String, Object> getCommonProperties() {

        return Collections.unmodifiableMap(commonProperties);
    }

    @Override
    public void registerHandler(String handlerName, DbHandler dbHandler) {
        Assert.notNull(dbHandler, "Parameter \"dbHandler\" must not null. ");
        Assert.notBlank(handlerName, "Parameter \"name\" must not blank. ");
        String className = dbHandler.getClass().getName();
        dbHandler.setCommonProperties(getCommonProperties());
        handlers.put(handlerName, dbHandler);
        log.info("Register the database handler \"{}\" to \"{}\". ", className, handlerName);
    }

    @Override
    public void deregisterHandler(String handlerName) {
        Assert.notBlank(handlerName, "Parameter \"name\" must not blank. ");
        DbHandler remove = handlers.remove(handlerName);
        if (remove != null) {
            String className = remove.getClass().getName();
            log.info("Deregister the database handler \"{}\" from \"{}\". ", className, handlerName);
        }
    }

    @Override
    public DbHandler getDbHandler(String handlerName) {
        Assert.notBlank(handlerName, "Parameter \"name\" must not blank. ");
        DbHandler dbHandler = handlers.get(handlerName);
        Assert.notNull(dbHandler
                , "The corresponding database handler could not be found by name. ");
        return dbHandler;
    }

    @Override
    public Object execute(String handlerName, Object[] arguments) {

        return getDbHandler(handlerName).execute(arguments);
    }

    @Override
    public <T> T execute(String handlerName, Object input, String operation, Type type) {

        return ObjectUtils.cast(execute(handlerName, new Object[] { operation, input, type }));
    }

}
