package artoria.generator.id.support;

import artoria.generator.id.IdGenerator;

/**
 * The identifier generator of type string.
 * @author Kahle
 */
public interface StringIdGenerator extends IdGenerator {

    /**
     * Generate the next identifier of type string.
     * @param arguments The arguments at generation time
     * @return The next identifier of type string
     */
    @Override
    String next(Object... arguments);

}
