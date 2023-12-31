package artoria.convert;

import java.lang.reflect.Type;

/**
 * The service interface for type conversion.
 * This is the entry point into the convert system.
 * @author Kahle
 */
public interface ConversionService {

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
     * Gets a converter that converts the source type to the target type.
     * @param sourceType The source type to convert from
     * @param targetType The target type to convert to
     * @return A type converter who can handle it or null
     */
    GenericConverter getConverter(Type sourceType, Type targetType);

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
