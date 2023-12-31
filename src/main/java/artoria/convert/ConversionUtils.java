package artoria.convert;

import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.util.Assert;
import artoria.util.ObjectUtils;

import java.lang.reflect.Type;

/**
 * The type conversion tools.
 * @author Kahle
 */
public class ConversionUtils {
    private static final Logger log = LoggerFactory.getLogger(ConversionUtils.class);
    private static volatile ConversionService conversionService;

    public static ConversionService getConversionService() {
        if (conversionService != null) { return conversionService; }
        synchronized (ConversionUtils.class) {
            if (conversionService != null) { return conversionService; }
            ConversionUtils.setConversionService(new SimpleConversionService());
            return conversionService;
        }
    }

    public static void setConversionService(ConversionService conversionService) {
        Assert.notNull(conversionService, "Parameter \"conversionService\" must not null. ");
        log.info("Set type conversion service: {}", conversionService.getClass().getName());
        ConversionUtils.conversionService = conversionService;
    }

    public static void register(GenericConverter converter) {

        getConversionService().addConverter(converter);
    }

    public static void deregister(GenericConverter converter) {

        getConversionService().removeConverter(converter);
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
        return ObjectUtils.cast(convert(source, (Type) targetType), targetType);
    }

}
