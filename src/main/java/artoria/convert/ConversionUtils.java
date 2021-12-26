package artoria.convert;

import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.util.Assert;

import java.lang.reflect.Type;

/**
 * The type conversion tools.
 * @author Kahle
 */
public class ConversionUtils {
    private static Logger log = LoggerFactory.getLogger(ConversionUtils.class);
    private static ConversionProvider conversionProvider;

    public static ConversionProvider getConversionProvider() {
        if (conversionProvider != null) { return conversionProvider; }
        synchronized (ConversionUtils.class) {
            if (conversionProvider != null) { return conversionProvider; }
            ConversionUtils.setConversionProvider(new SimpleConversionProvider());
            return conversionProvider;
        }
    }

    public static void setConversionProvider(ConversionProvider conversionProvider) {
        Assert.notNull(conversionProvider, "Parameter \"conversionProvider\" must not null. ");
        log.info("Set type conversion provider: {}", conversionProvider.getClass().getName());
        ConversionUtils.conversionProvider = conversionProvider;
    }

    public static void register(GenericConverter converter) {

        getConversionProvider().addConverter(converter);
    }

    public static void deregister(GenericConverter converter) {

        getConversionProvider().removeConverter(converter);
    }

    public static boolean canConvert(Type sourceType, Type targetType) {

        return getConversionProvider().canConvert(sourceType, targetType);
    }

    public static Object convert(Object source, Type targetType) {

        return getConversionProvider().convert(source, targetType);
    }

    public static Object convert(Object source, Type sourceType, Type targetType) {

        return getConversionProvider().convert(source, sourceType, targetType);
    }

}
