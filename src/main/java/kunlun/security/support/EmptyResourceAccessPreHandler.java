/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.security.support;

import kunlun.core.handler.ResourceAccessPreHandler;

/**
 * The empty resource access pre handler.
 * @author Kahle
 */
public class EmptyResourceAccessPreHandler implements ResourceAccessPreHandler {

    @Override
    public Object handle(Object type, Object resource, String token, Object... arguments) {

        return true;
    }

}
