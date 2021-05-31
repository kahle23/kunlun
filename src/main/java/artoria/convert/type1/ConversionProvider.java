package artoria.convert.type1;

import java.lang.reflect.Type;

/**
 * The provider interface for the type conversion service.
 * @author Kahle
 */
public interface ConversionProvider {

    /**
     * Add a converter to this provider.
     * @param converter A type converter
     */
    void addConverter(GenericConverter converter);

    /**
     * Remove a converter from this provider.
     * @param converter A type converter
     */
    void removeConverter(GenericConverter converter);

    /**
     * Return true if objects of source type can be converted to the target type.
     * @param sourceType The source type to convert from
     * @param targetType The target type to convert to
     * @return True if conversion can be performed, false otherwise
     */
    boolean canConvert(Type sourceType, Type targetType);

    /**
     * Convert the given source object to the specified target type.
     * @param source The source object to convert
     * @param targetType The target type to convert to
     * @return The converted object or source object
     */
    Object convert(Object source, Type targetType);

    /**
     * Convert the source object to the target type.
     * @param source The source object to convert
     * @param sourceType The source type to convert from
     * @param targetType The target type to convert to
     * @return The converted object or source object
     */
    Object convert(Object source, Type sourceType, Type targetType);

}
