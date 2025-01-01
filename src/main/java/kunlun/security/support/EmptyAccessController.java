/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.security.support;

import kunlun.core.AccessController;

/**
 * The empty access controller.
 * @author Kahle
 */
public class EmptyAccessController implements AccessController {

    @Override
    public boolean hasPermission(Object userId, Object userType, String permission) {

        return true;
    }

}
