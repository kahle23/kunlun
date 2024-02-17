/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.convert;

import java.lang.reflect.Type;

/**
 * The conversions are supported based on the source type and target type.
 * @author Kahle
 */
public interface ConditionalConverter extends GenericConverter {

    /**
     * Return true if objects of source type can be converted to the target type.
     * @param sourceType The source type to convert from
     * @param targetType The target type to convert to
     * @return True if conversion should be performed, false otherwise
     */
    boolean matches(Type sourceType, Type targetType);

}
