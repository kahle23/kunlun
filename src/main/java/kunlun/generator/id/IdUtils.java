/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.generator.id;

import kunlun.logging.Logger;
import kunlun.logging.LoggerFactory;
import kunlun.util.Assert;

/**
 * The identifier generation tools.
 * @author Kahle
 */
public class IdUtils {
    private static final Logger log = LoggerFactory.getLogger(IdUtils.class);
    private static volatile IdProvider idProvider;

    public static IdProvider getIdProvider() {
        if (idProvider != null) { return idProvider; }
        synchronized (IdUtils.class) {
            if (idProvider != null) { return idProvider; }
            IdUtils.setIdProvider(new SimpleIdProvider());
            return idProvider;
        }
    }

    public static void setIdProvider(IdProvider idProvider) {
        Assert.notNull(idProvider, "Parameter \"idProvider\" must not null. ");
        log.info("Set id provider: {}", idProvider.getClass().getName());
        IdUtils.idProvider = idProvider;
    }

    public static void registerGenerator(String name, IdGenerator idGenerator) {

        getIdProvider().registerGenerator(name, idGenerator);
    }

    public static void deregisterGenerator(String name) {

        getIdProvider().deregisterGenerator(name);
    }

    public static IdGenerator getIdGenerator(String name) {

        return getIdProvider().getIdGenerator(name);
    }

    public static Object next(String name, Object... arguments) {

        return getIdProvider().next(name, arguments);
    }

    public static String nextString(String name, Object... arguments) {

        return (String) getIdProvider().next(name, arguments);
    }

    public static Long nextLong(String name, Object... arguments) {

        return (Long) getIdProvider().next(name, arguments);
    }

}
