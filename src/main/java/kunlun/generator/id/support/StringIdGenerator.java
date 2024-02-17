/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.generator.id.support;

import kunlun.generator.id.IdGenerator;

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
