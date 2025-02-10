/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.security;

import java.io.Serializable;
import java.util.Collection;

/**
 * The user manager.
 * @author Kahle
 */
public interface UserManager {

    /**
     * Get the user detail based on user id.
     * @param userId The unique identification of the user
     * @param userType The user type (for example, toC user, toB user, or admin user)
     * @return The user detail
     */
    UserDetail getUserDetail(Object userId, Object userType);

    /**
     * Get the user roles. // todo
     * @param userId The unique identification of the user
     * @param userType The user type (for example, toC user, toB user, or admin user)
     * @return The user roles
     */
    Collection<String> getUserRoles(Object userId, String userType);

    /**
     * Get the user permissions. // todo
     * @param userId The unique identification of the user
     * @param userType The user type (for example, toC user, toB user, or admin user)
     * @return The user permissions
     */
    Collection<String> getUserPermissions(Object userId, String userType);

    /**
     * Gets a list of the groups to which the user belongs.
     * @param userId The unique identification of the user
     * @param userType The user type (for example, toC user, toB user, or admin user)
     * @param groupType The user group type, such as department, region, etc
     * @return The groups list
     */
    Collection<String> getUserGroups(Object userId, Object userType, Object groupType);


    /**
     * The interface for the definition of user detail.
     * @author Kahle
     */
    interface UserDetail extends Serializable {

        /**
         * Get the user id.
         * @return The user id
         */
        Object getUserId();

        /**
         * Get the user type.
         * @return The user type
         */
        Object getUserType();

        /**
         * Get the username.
         * @return The username
         */
        String getUsername();

        /**
         * Get the user display name.
         * @return The user display name
         */
        String getDisplayName();

        /**
         * Indicates whether the user is enabled or disabled.
         * @return True if the user is enabled, false otherwise
         */
        Boolean getEnabled();

    }

}
