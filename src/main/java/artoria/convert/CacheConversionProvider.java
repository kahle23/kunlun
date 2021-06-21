package artoria.convert;

import artoria.cache.CacheUtils;
import artoria.convert.GenericConverter.ConvertiblePair;
import artoria.util.Assert;
import artoria.util.ObjectUtils;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static artoria.common.Constants.THREE;
import static artoria.common.Constants.ZERO;

/**
 * The cache conversion service provider (static proxy).
 * @author Kahle
 */
public class CacheConversionProvider extends AbstractConversionProvider {
    private final ConversionProvider conversionProvider;
    private final String cacheName;
    private final TimeUnit timeUnit;
    private final Long typeTimeToLive;
    private final Long timeToLive;

    public CacheConversionProvider(ConversionProvider conversionProvider,
                                   String cacheName,
                                   Long timeToLive,
                                   TimeUnit timeUnit) {
        Assert.notNull(conversionProvider, "Parameter \"conversionProvider\" must not null. ");
        Assert.notBlank(cacheName, "Parameter \"cacheName\" must not blank. ");
        Assert.notNull(timeToLive, "Parameter \"timeToLive\" must not null. ");
        Assert.notNull(timeUnit, "Parameter \"timeUnit\" must not null. ");
        Assert.isTrue(timeToLive >= ZERO, "Parameter \"timeToLive\" must >= 0. ");
        this.conversionProvider = conversionProvider;
        this.cacheName = cacheName;
        this.typeTimeToLive = timeToLive * THREE;
        this.timeToLive = timeToLive;
        this.timeUnit = timeUnit;
    }

    @Override
    protected List<Class<?>> getClassHierarchy(Class<?> type) {
        Assert.notNull(type, "Parameter \"type\" must not null. ");
        String cacheKey = "cache_class_hierarchy[" + type + "]";
        List<Class<?>> result = ObjectUtils.cast(CacheUtils.get(cacheName, cacheKey));
        if (result != null) { return result; }
        result = super.getClassHierarchy(type);
        if (result == null) { return null; }
        CacheUtils.put(cacheName, cacheKey, result, typeTimeToLive, timeUnit);
        return result;
    }

    @Override
    public void addConverter(GenericConverter converter) {

        conversionProvider.addConverter(converter);
    }

    @Override
    public void removeConverter(GenericConverter converter) {
        Assert.notNull(converter, "Parameter \"converter\" must not null. ");
        conversionProvider.removeConverter(converter);
        if (converter instanceof ConditionalConverter) {
            CacheUtils.clear(cacheName); return;
        }
        Set<ConvertiblePair> convertibleTypes = converter.getConvertibleTypes();
        Assert.notEmpty(convertibleTypes,
            "Only conditional converters may return empty convertible types. ");
        for (ConvertiblePair convertibleType : convertibleTypes) {
            Class<?> sourceType = convertibleType.getSourceType();
            Class<?> targetType = convertibleType.getTargetType();
            ConverterCacheKey cacheKey = new ConverterCacheKey(sourceType, targetType);
            CacheUtils.remove(cacheName, cacheKey);
        }
    }

    @Override
    public GenericConverter getConverter(Type sourceType, Type targetType) {
        Assert.notNull(sourceType, "Parameter \"sourceType\" must not null. ");
        Assert.notNull(targetType, "Parameter \"targetType\" must not null. ");
        ConverterCacheKey cacheKey = new ConverterCacheKey(sourceType, targetType);
        GenericConverter converter = CacheUtils.get(cacheName, cacheKey, GenericConverter.class);
        if (converter != null) { return converter; }
        converter = conversionProvider.getConverter(sourceType, targetType);
        if (converter == null) { return null; }
        CacheUtils.put(cacheName, cacheKey, converter, timeToLive, timeUnit);
        return converter;
    }

    /**
     * The class of the cached key at type conversion.
     */
    protected class ConverterCacheKey {
        private final Type sourceType;
        private final Type targetType;

        public ConverterCacheKey(Type sourceType, Type targetType) {
            Assert.notNull(sourceType, "The source type must not be null. ");
            Assert.notNull(targetType, "The target type must not be null. ");
            this.sourceType = sourceType;
            this.targetType = targetType;
        }

        @Override
        public boolean equals(Object object) {
            if (this == object) { return true; }
            if (object == null || getClass() != object.getClass()) { return false; }
            ConverterCacheKey that = (ConverterCacheKey) object;
            return sourceType == that.sourceType && targetType == that.targetType;
        }

        @Override
        public int hashCode() {

            return sourceType.hashCode() * 31 + targetType.hashCode();
        }

        @Override
        public String toString() {
            return "ConverterCacheKey{" +
                    "sourceType=" + sourceType +
                    ", targetType=" + targetType +
                    '}';
        }

    }

}
