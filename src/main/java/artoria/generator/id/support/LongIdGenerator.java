package artoria.generator.id.support;

import artoria.generator.id.IdGenerator;

/**
 * The identifier generator of type long.
 * @author Kahle
 */
public interface LongIdGenerator extends IdGenerator {

    /**
     * Generate the next identifier of type long.
     * @param arguments The arguments at generation time
     * @return The next identifier of type long
     */
    @Override
    Long next(Object... arguments);

}
