/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.security.support;

import kunlun.security.RbacAccessController;

/**
 * The empty RBAC access controller.
 * @author Kahle
 */
public class EmptyRbacAccessController extends EmptyAccessController implements RbacAccessController {

    @Override
    public boolean hasRoleAnd(Object userId, Object userType, String... roles) {

        return true;
    }

    @Override
    public boolean hasRoleOr(Object userId, Object userType, String... roles) {

        return true;
    }

}
