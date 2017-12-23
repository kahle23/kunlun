package apyh.artoria.converter;

/**
 * Type converter
 * @author Kahle
 */
public interface Converter {

    /**
     * Do type convert
     * @param source Will converting object
     * @param target Target type
     * @return Converted object
     */
    Object convert(Object source, Class<?> target);

}
