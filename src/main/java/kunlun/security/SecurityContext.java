/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.security;

import kunlun.core.AccessController;
import kunlun.core.Context;
import kunlun.core.DataController;

import java.util.Collection;

import static kunlun.security.UserManager.UserDetail;

/**
 * The security context.
 * (Providing functions such as logged-in user information retrieval and permission verification)
 * @author Kahle
 */
public interface SecurityContext extends Context {

    /**
     * Get the current trace id.
     * @return The current trace id
     */
    String getTraceId();

    /**
     * Get the current token.
     * @return The current token
     */
    String getToken();

    /**
     * Get the current user id.
     * @return The current user id
     */
    Object getUserId();

    /**
     * Get the current user type.
     * @return The current user type
     */
    Object getUserType();

    /**
     * Get the current platform.
     * @return The current platform
     */
    String getPlatform();

    /**
     * Get the current tenant id.
     * @return The current tenant id
     */
    String getTenantId();

    /**
     * Put the base data.
     * @param userId   The current user id
     * @param userType The current user type
     * @param platform The current platform
     * @param tenantId The current tenant id
     */
    void putBaseData(Object userId, Object userType, String platform, String tenantId);

    // ====

    /**
     * Get the current user detail.
     * @return The current user detail
     */
    UserDetail getUserDetail();

    Collection<String> getUserGroups(Object groupType);

    // ====

    /**
     * Get the held access controller.
     * @return The held access controller
     */
    AccessController getAccessController();

    DataController getDataController();

    /**
     * Get the held token manager.
     * @return The held token manager
     */
    TokenManager getTokenManager();

    /**
     * Get the held user manager.
     * @return The held user manager
     */
    UserManager getUserManager();

}
