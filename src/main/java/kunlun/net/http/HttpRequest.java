/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.net.http;

import kunlun.data.tuple.KeyValue;

import java.util.Collection;

/**
 * The http request.
 * @author Kahle
 */
public interface HttpRequest extends HttpClient.HttpBase {

    /**
     * Get the http parameters.
     * @return The http parameters
     */
    Collection<KeyValue<String, Object>> getParameters();

    /**
     * Get the http body.
     * @return The http body
     */
    Object getBody();

}
