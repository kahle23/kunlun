/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.security;

import kunlun.core.AccessController;

/**
 * The RBAC access controller.
 *
 * RBAC allows access based on the job title. RBAC largely eliminates discretion when providing access to objects.
 *
 * For example,
 *      a human resources specialist should not have permissions to create network accounts;
 *      this should be a role reserved for network administrators.
 *
 * Why not provide a way to get roles based on user id?
 * Because the roles associated with the user may be expressions, such as regular expressions.
 *
 * @author Kahle
 */
public interface RbacAccessController extends AccessController {

    /**
     * Judge whether the user has role.
     * @param userId The unique identification of the user
     * @param userType The user type (can null) (for example, toC user, toB user, or admin user)
     * @param role The roles identifier to be judged
     * @return The result of judgment
     */
    boolean hasRole(Object userId, Object userType, String role);

}
