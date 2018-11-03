package artoria.identity;

/**
 * Id generator.
 * @author Kahle
 */
public interface IdGenerator<T> {

    /**
     * Random generate the generic type identity.
     * @return The generic type identity
     */
    T next();

}
