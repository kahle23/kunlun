package artoria.convert;

import artoria.convert.support.*;
import artoria.util.Assert;
import artoria.util.ClassUtils;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static artoria.common.Constants.*;
import static artoria.util.ClassUtils.getWrapper;
import static java.lang.Boolean.FALSE;

/**
 * The abstract conversion service.
 * @author Kahle
 */
public abstract class AbstractConversionService implements ConversionService {

    /**
     * Add some default type converters.
     */
    protected void addDefaultConverters() {
        addConverter(new ObjectToStringConverter());
        addConverter(new StringToBooleanConverter());
        addConverter(new StringToNumberConverter());
        addConverter(new StringToDateConverter());
        addConverter(new DateToStringConverter());
        addConverter(new DateToDateConverter());
        addConverter(new NumberToNumberConverter());
        addConverter(new NumberToDateConverter());
    }

    private void addToClassHierarchy(Class<?> type, int index, boolean isArray,
                                     Set<Class<?>> visited, List<Class<?>> hierarchy) {
        if (isArray) {
            Object instance = Array.newInstance(type, ZERO);
            type = instance.getClass();
        }
        if (visited.add(type)) { hierarchy.add(index, type); }
    }

    /**
     * Gets the hierarchy of the class. This method can handle enumerations and interfaces.
     * @param type The class to get
     * @return A list of hierarchies of classes
     */
    protected List<Class<?>> getClassHierarchy(Class<?> type) {
        Assert.notNull(type, "Parameter \"type\" must not null. ");
        List<Class<?>> hierarchy = new ArrayList<Class<?>>(TWENTY);
        Set<Class<?>> visited = new HashSet<Class<?>>(TWENTY);
        boolean isArray = type.isArray();
        addToClassHierarchy(getWrapper(type), ZERO, FALSE, visited, hierarchy);
        // Handle the ordinary bean.
        for (int i = ZERO; i < hierarchy.size(); i++) {
            Class<?> candidate = hierarchy.get(i);
            candidate = isArray ? candidate.getComponentType() : getWrapper(candidate);
            Class<?> superclass = candidate.getSuperclass();
            boolean flag = superclass != null &&
                    superclass != Object.class && superclass != Enum.class;
            if (flag) {
                addToClassHierarchy(superclass, (i + ONE), isArray, visited, hierarchy);
            }
            Class<?>[] interfaces = candidate.getInterfaces();
            for (Class<?> clazz : interfaces) {
                addToClassHierarchy(clazz, hierarchy.size(), isArray, visited, hierarchy);
            }
        }
        // Handle the enumeration.
        if (Enum.class.isAssignableFrom(type)) {
            addToClassHierarchy(Enum.class, hierarchy.size(), isArray, visited, hierarchy);
            addToClassHierarchy(Enum.class, hierarchy.size(), FALSE, visited, hierarchy);
            Class<?>[] interfaces = Enum.class.getInterfaces();
            for (Class<?> clazz : interfaces) {
                addToClassHierarchy(clazz, hierarchy.size(), isArray, visited, hierarchy);
            }
        }
        // Handle the 'Object'.
        addToClassHierarchy(Object.class, hierarchy.size(), isArray, visited, hierarchy);
        addToClassHierarchy(Object.class, hierarchy.size(), FALSE, visited, hierarchy);
        return hierarchy;
    }

    /**
     * Whether the target type can be assigned from the source type.
     * @param sourceType The source type to convert from
     * @param targetType The target type to convert to
     * @return The result of whether it can be assigned
     */
    protected boolean assignable(Type sourceType, Type targetType) {
        if (sourceType == targetType) { return true; }
        if (sourceType instanceof Class && targetType instanceof Class) {
            Class<?> sourceClass = ClassUtils.getWrapper((Class) sourceType);
            Class<?> targetClass = ClassUtils.getWrapper((Class) targetType);
            return targetClass.isAssignableFrom(sourceClass);
        }
        return false;
    }

    @Override
    public boolean canConvert(Type sourceType, Type targetType) {
        Assert.notNull(targetType, "Target type to convert to cannot be null. ");
        if (sourceType == null) { return true; }
        if (assignable(sourceType, targetType)) { return true; }
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
        if (assignable(sourceType, targetType)) { return source; }
        GenericConverter converter = getConverter(sourceType, targetType);
        if (converter == null) { return source; }
        return converter.convert(source, sourceType, targetType);
    }

}
