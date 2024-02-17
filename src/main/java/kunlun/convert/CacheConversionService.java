/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.convert;

import kunlun.cache.CacheUtils;
import kunlun.convert.GenericConverter.ConvertiblePair;
import kunlun.util.Assert;
import kunlun.util.ObjectUtils;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Set;

/**
 * The cache conversion service (static proxy).
 * @author Kahle
 */
public class CacheConversionService extends AbstractConversionService {
    private final ConversionService conversionService;
    private final String cacheName;

    public CacheConversionService(ConversionService conversionService, String cacheName) {
        Assert.notNull(conversionService, "Parameter \"conversionService\" must not null. ");
        Assert.notBlank(cacheName, "Parameter \"cacheName\" must not blank. ");
        this.conversionService = conversionService;
        this.cacheName = cacheName;
    }

    @Override
    protected List<Class<?>> getClassHierarchy(Class<?> type) {
        Assert.notNull(type, "Parameter \"type\" must not null. ");
        String cacheKey = "cache_class_hierarchy[" + type + "]";
        List<Class<?>> result = ObjectUtils.cast(CacheUtils.get(cacheName, cacheKey));
        if (result != null) { return result; }
        result = super.getClassHierarchy(type);
        if (result == null) { return null; }
        CacheUtils.put(cacheName, cacheKey, result);
        return result;
    }

    @Override
    public void registerConverter(GenericConverter converter) {

        conversionService.registerConverter(converter);
    }

    @Override
    public void deregisterConverter(GenericConverter converter) {
        Assert.notNull(converter, "Parameter \"converter\" must not null. ");
        conversionService.deregisterConverter(converter);
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
        converter = conversionService.getConverter(sourceType, targetType);
        if (converter == null) { return null; }
        CacheUtils.put(cacheName, cacheKey, converter);
        return converter;
    }

    /**
     * The class of the cached key at type conversion.
     * @author Kahle
     */
    protected static class ConverterCacheKey {
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
