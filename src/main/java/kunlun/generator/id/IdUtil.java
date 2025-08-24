/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.generator.id;

import kunlun.generator.id.support.uuid.UUIDGenerator;
import kunlun.logging.Logger;
import kunlun.logging.LoggerFactory;
import kunlun.util.Assert;

import static kunlun.common.constant.Algorithms.UUID;
import static kunlun.convert.ConversionUtil.convert;

/**
 * The identifier generation tools.
 * @author Zerox
 */
public class IdUtil {
    private static final Logger log = LoggerFactory.getLogger(IdUtil.class);
    private static volatile IdProvider idProvider;

    public static IdProvider getIdProvider() {
        if (idProvider != null) { return idProvider; }
        synchronized (IdUtil.class) {
            if (idProvider != null) { return idProvider; }
            IdUtil.setIdProvider(new SimpleIdProvider());
            // Register the uuid generator.
            registerGenerator(UUID, new UUIDGenerator());
            return idProvider;
        }
    }

    public static void setIdProvider(IdProvider idProvider) {
        Assert.notNull(idProvider, "Parameter \"idProvider\" must not null. ");
        log.debug("Set id provider: {}", idProvider.getClass().getName());
        IdUtil.idProvider = idProvider;
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

    public static Object preview(String name, Object... arguments) {

        return getIdProvider().preview(name, arguments);
    }

    public static Object next(String name, Object... arguments) {

        return getIdProvider().next(name, arguments);
    }

    public static String previewString(String name, Object... arguments) {

        return convert(getIdProvider().preview(name, arguments), String.class);
    }

    public static Long previewLong(String name, Object... arguments) {

        return convert(getIdProvider().preview(name, arguments), Long.class);
    }

    public static String nextString(String name, Object... arguments) {

        return convert(getIdProvider().next(name, arguments), String.class);
    }

    public static Long nextLong(String name, Object... arguments) {

        return convert(getIdProvider().next(name, arguments), Long.class);
    }

}
