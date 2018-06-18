package artoria.converter;

/**
 * Converter.
 * @author Kahle
 */
public interface Converter {

    /**
     * Do convert
     * @param source Will converting object
     * @param target Target type
     * @return Converted object
     */
    Object convert(Object source, Class<?> target);

}
