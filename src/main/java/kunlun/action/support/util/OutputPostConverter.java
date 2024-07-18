/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.action.support.util;

import kunlun.core.function.BiFunction;
import kunlun.data.Dict;
import kunlun.data.bean.BeanUtils;
import kunlun.util.ClassUtils;

import java.util.Collection;
import java.util.Map;

/**
 * The output object post-converter.
 * @author Kahle
 */
public class OutputPostConverter implements BiFunction<Object, Class<?>, Object> {

    @Override
    public Object apply(Object output, Class<?> expectedClass) {
        // Check for null.
        if (output == null) { return null; }
        // Simple value type, no processing.
        if (ClassUtils.isSimpleValueType(expectedClass)) {
            return output;
        }
        // Try to perform type conversion.
        if (output instanceof Collection) {
            return output;
        }
        else if (output.getClass().isArray()) {
            return output;
        }
        else if (output instanceof Map && Map.class.isAssignableFrom(expectedClass)) {
            return Dict.of((Map<?, ?>) output);
        }
        else if (!expectedClass.isAssignableFrom(output.getClass())) {
            return BeanUtils.beanToBean(output, expectedClass);
        }
        else { return output; }
    }

}
