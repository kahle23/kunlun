/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.generator.id.support;

import kunlun.generator.id.IdGenerator;

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
