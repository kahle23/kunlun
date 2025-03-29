/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.convert;

import kunlun.logging.Logger;
import kunlun.logging.LoggerFactory;
import kunlun.util.Assert;
import kunlun.util.ObjUtil;

import java.lang.reflect.Type;

/**
 * The type conversion tools.
 * @author Kahle
 */
public class ConversionUtil {
    private static final Logger log = LoggerFactory.getLogger(ConversionUtil.class);
    private static volatile ConversionService conversionService;

    public static ConversionService getConversionService() {
        if (conversionService != null) { return conversionService; }
        synchronized (ConversionUtil.class) {
            if (conversionService != null) { return conversionService; }
            ConversionUtil.setConversionService(new SimpleConversionService());
            return conversionService;
        }
    }

    public static void setConversionService(ConversionService conversionService) {
        Assert.notNull(conversionService, "Parameter \"conversionService\" must not null. ");
        log.debug("Set type conversion service: {}", conversionService.getClass().getName());
        ConversionUtil.conversionService = conversionService;
    }

    public static void registerConverter(GenericConverter converter) {

        getConversionService().registerConverter(converter);
    }

    public static void deregisterConverter(GenericConverter converter) {

        getConversionService().deregisterConverter(converter);
    }

    public static boolean canConvert(Type sourceType, Type targetType) {

        return getConversionService().canConvert(sourceType, targetType);
    }

    public static Object convert(Object source, Type targetType) {

        return getConversionService().convert(source, targetType);
    }

    public static Object convert(Object source, Type sourceType, Type targetType) {

        return getConversionService().convert(source, sourceType, targetType);
    }

    public static <T> T convert(Object source, Class<T> targetType) {
        // Make sure that the target type can be converted successfully,
        // Otherwise, a cast exception will occur.
        return ObjUtil.cast(convert(source, (Type) targetType), targetType);
    }

}
