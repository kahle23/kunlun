/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.data;

import kunlun.core.Definition;

/**
 * The code definition.
 *
 * In communications and information processing, code is a system of rules to convert information (such as
 * a letter, word, sound, image, or gesture) into another form, sometimes shortened or secret, for
 * communication through a communication channel or storage in a storage medium.
 *
 * @see <a href="https://en.wikipedia.org/wiki/Code">Code</a>
 * @see <a href="https://en.wikipedia.org/wiki/Error_code">Error code</a>
 * @author Kahle
 */
public interface CodeDefinition extends Definition {

    /**
     * The value of the code.
     * @return The value
     */
    Object getCode();

    /**
     * The description of the code.
     * @return The description
     */
    String getDescription();

}
