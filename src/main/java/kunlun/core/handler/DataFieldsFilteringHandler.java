/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.core.handler;

import kunlun.core.Handler;

/**
 * The data fields filtering handler.
 * It is generally used for the processing of the result of the invoked
 *      (usually in the case of data permissions).
 * @author Kahle
 */
public interface DataFieldsFilteringHandler extends Handler {

    /**
     * Filter data fields.
     * @param type It is used to distinguish fields filtering in different scenarios
     * @param data The data to be filtered
     * @param arguments The arguments that may be used during filtering
     * @return The filtered data
     */
    Object filter(Object type, Object data, Object... arguments);

}
