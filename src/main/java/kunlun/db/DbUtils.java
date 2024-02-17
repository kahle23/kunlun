/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.db;

import kunlun.logging.Logger;
import kunlun.logging.LoggerFactory;
import kunlun.util.Assert;

import java.lang.reflect.Type;

/**
 * The database tools.
 * @author Kahle
 */
public class DbUtils {
    private static final Logger log = LoggerFactory.getLogger(DbUtils.class);
    private static volatile DbProvider dbProvider;

    public static DbProvider getDbProvider() {
        if (dbProvider != null) { return dbProvider; }
        synchronized (DbUtils.class) {
            if (dbProvider != null) { return dbProvider; }
            DbUtils.setDbProvider(new SimpleDbProvider());
            return dbProvider;
        }
    }

    public static void setDbProvider(DbProvider dbProvider) {
        Assert.notNull(dbProvider, "Parameter \"ocrProvider\" must not null. ");
        log.info("Set database provider: {}", dbProvider.getClass().getName());
        DbUtils.dbProvider = dbProvider;
    }

    public static void registerHandler(String handlerName, DbHandler dbHandler) {

        getDbProvider().registerHandler(handlerName, dbHandler);
    }

    public static void deregisterHandler(String handlerName) {

        getDbProvider().deregisterHandler(handlerName);
    }

    public static DbHandler getDbHandler(String handlerName) {

        return getDbProvider().getDbHandler(handlerName);
    }

    public static Object execute(String handlerName, Object[] arguments) {

        return getDbProvider().execute(handlerName, arguments);
    }

    public static <T> T execute(String handlerName, Object input, String operation, Type type) {

        return getDbProvider().execute(handlerName, input, operation, type);
    }

}
