package artoria.convert.type1;

import artoria.convert.type1.GenericConverter.ConvertiblePair;
import artoria.convert.type1.support.*;
import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.util.Assert;
import artoria.util.ClassUtils;
import artoria.util.CollectionUtils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingDeque;

import static artoria.common.Constants.FORTY;

/**
 * Simple type conversion service provider.
 * @author Kahle
 */
public class SimpleConversionProvider implements ConversionProvider {
    private static Logger log = LoggerFactory.getLogger(SimpleConversionProvider.class);
    protected final Map<ConvertiblePair, ConverterGroup> converterMap;
    protected final Set<GenericConverter> globalConverters;

    public SimpleConversionProvider() {
        this(
                new ConcurrentHashMap<ConvertiblePair, ConverterGroup>(FORTY),
                new ConcurrentHashMap<GenericConverter, Boolean>().keySet(Boolean.TRUE)
        );
    }

    protected SimpleConversionProvider(Map<ConvertiblePair, ConverterGroup> converterMap,
                                       Set<GenericConverter> globalConverters) {
        this.globalConverters = globalConverters;
        this.converterMap = converterMap;
        converters();
    }

    protected void converters() {
        addConverter(new ObjectToStringConverter(this));
        addConverter(new StringToBooleanConverter(this));
        addConverter(new StringToNumberConverter(this));
        addConverter(new StringToDateConverter(this));
        addConverter(new DateToStringConverter(this));
        addConverter(new DateToDateConverter(this));
        addConverter(new NumberToNumberConverter(this));
        addConverter(new NumberToDateConverter(this));
    }

    protected Class<?> primaryClass(Type type) {
        if (type == null) { return null; }
        if (type instanceof Class) {
            return (Class<?>) type;
        }
        if (type instanceof ParameterizedType) {
            ParameterizedType paramType = (ParameterizedType) type;
            Type rawType = paramType.getRawType();
            return (Class<?>) rawType;
        }
        /*if (type instanceof TypeVariable) {
            ((TypeVariable) type).getBounds();
        }
        if (type instanceof WildcardType) {
            ((WildcardType) type).getLowerBounds();
            ((WildcardType) type).getUpperBounds();
        }
        if (type instanceof GenericArrayType) {
            ((GenericArrayType) type).getGenericComponentType();
        }*/
        return null;
    }

    protected GenericConverter getConverter(Type sourceType, Type targetType) {
        // Convert 'Type' to 'Class'.
        Class<?> sourceClass = primaryClass(sourceType);
        Class<?> targetClass = primaryClass(targetType);
        // Get the full type hierarchy.
        List<Class<?>> sourceCandidates = ClassUtils.getClassHierarchy(sourceClass);
        List<Class<?>> targetCandidates = ClassUtils.getClassHierarchy(targetClass);
        // Types traversal.
        for (Class<?> sourceCandidate : sourceCandidates) {
            for (Class<?> targetCandidate : targetCandidates) {
                ConvertiblePair pair = new ConvertiblePair(sourceCandidate, targetCandidate);
                // Check specifically registered converters.
                ConverterGroup converterGroup = converterMap.get(pair);
                if (converterGroup != null) {
                    GenericConverter converter = converterGroup.get(sourceType, targetType);
                    if (converter != null) { return converter; }
                }
                // Check ConditionalConverters for a dynamic match.
                for (GenericConverter globalConverter : globalConverters) {
                    ConditionalConverter converter = (ConditionalConverter) globalConverter;
                    if (converter.matches(sourceType, targetType)) { return globalConverter; }
                }
                // Can't match.
            }
        }
        return null;
    }

    @Override
    public void addConverter(GenericConverter converter) {
        Assert.notNull(converter, "Parameter \"converter\" must not null. ");
        log.info("Add type converter: {}", converter.getClass().getName());
        Set<ConvertiblePair> convertibleTypes = converter.getConvertibleTypes();
        if (CollectionUtils.isEmpty(convertibleTypes)) {
            Assert.state(converter instanceof ConditionalConverter,
                    "Only conditional converters may return empty convertible types. ");
            globalConverters.add(converter); return;
        }
        for (ConvertiblePair convertiblePair : convertibleTypes) {
            ConverterGroup converterGroup = converterMap.get(convertiblePair);
            if (converterGroup == null) {
                converterMap.put(convertiblePair, (converterGroup = new ConverterGroup()));
            }
            converterGroup.add(converter);
        }
    }

    @Override
    public void removeConverter(GenericConverter converter) {
        Assert.notNull(converter, "Parameter \"converter\" must not null. ");
        log.info("Remove type converter: {}", converter.getClass().getName());
        Set<ConvertiblePair> convertibleTypes = converter.getConvertibleTypes();
        if (CollectionUtils.isEmpty(convertibleTypes)) {
            Assert.state(converter instanceof ConditionalConverter,
                    "Only conditional converters may return empty convertible types. ");
            globalConverters.remove(converter); return;
        }
        for (ConvertiblePair convertiblePair : convertibleTypes) {
            ConverterGroup converterGroup = converterMap.get(convertiblePair);
            if (converterGroup == null) { continue; }
            converterGroup.remove(converter);
        }
    }

    @Override
    public boolean canConvert(Type sourceType, Type targetType) {
        Assert.notNull(targetType, "Target type to convert to cannot be null. ");
        if (sourceType == null) { return true; }
        GenericConverter converter = getConverter(sourceType, targetType);
        return converter != null;
    }

    @Override
    public Object convert(Object source, Type targetType) {

        return convert(source, null, targetType);
    }

    @Override
    public Object convert(Object source, Type sourceType, Type targetType) {
        Assert.notNull(targetType, "Target type to convert to cannot be null. ");
        if (source == null) { return null; }
        if (sourceType == null) { sourceType = source.getClass(); }
        GenericConverter converter = getConverter(sourceType, targetType);
        if (converter == null) { return source; }
        return converter.convert(source, sourceType, targetType);
    }

    protected static class ConverterGroup {
        private final Deque<GenericConverter> converters;

        public ConverterGroup() {

            this(new LinkedBlockingDeque<GenericConverter>());
        }

        public ConverterGroup(Deque<GenericConverter> converters) {
            Assert.notNull(converters, "Parameter \"converters\" must not null. ");
            this.converters = converters;
        }

        public void add(GenericConverter converter) {

            converters.addFirst(converter);
        }

        public void remove(GenericConverter converter) {

            converters.remove(converter);
        }

        public GenericConverter get(Type sourceType, Type targetType) {
            for (GenericConverter converter : converters) {
                if (!(converter instanceof ConditionalConverter) ||
                        ((ConditionalConverter) converter).matches(sourceType, targetType)) {
                    return converter;
                }
            }
            return null;
        }

    }

}
