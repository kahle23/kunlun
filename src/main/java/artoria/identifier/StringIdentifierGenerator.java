package artoria.identifier;

/**
 * String identifier generator.
 * @author Kahle
 */
public interface StringIdentifierGenerator extends IdentifierGenerator {

    /**
     * Randomly generate the next string identifier.
     * @return Next string identifier
     */
    String nextStringIdentifier();

}
