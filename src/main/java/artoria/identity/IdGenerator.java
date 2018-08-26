package artoria.identity;

/**
 * Id generator.
 * @author Kahle
 */
public interface IdGenerator {

    /**
     * Randomly generate the number identity.
     * @return Number identity
     */
    Number nextNumber();

    /**
     * Randomly generate the string identity.
     * @return String identity
     */
    String nextString();

}
