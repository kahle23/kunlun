/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.security.support;

import kunlun.security.UserManager;

import java.util.Collection;

/**
 * The abstract user manager.
 * @author Kahle
 */
public abstract class AbstractUserManager implements UserManager {

    @Override
    public Collection<String> getUserRoles(Object userId, String userType) {

        return null;
    }

    @Override
    public Collection<String> getUserPermissions(Object userId, String userType) {

        return null;
    }

    @Override
    public Collection<String> getUserGroups(Object userId, Object userType, Object groupType) {

        return null;
    }

    /**
     * The simple implementation class for UserDetail.
     * @author Kahle
     */
    public static class UserImpl implements UserDetail {
        private Object userId;
        private Object userType;
        private String username;
        private String displayName;
        private Boolean enabled;

        @Override
        public Object getUserId() {

            return userId;
        }

        public void setUserId(Object userId) {

            this.userId = userId;
        }

        @Override
        public Object getUserType() {

            return userType;
        }

        public void setUserType(Object userType) {

            this.userType = userType;
        }

        @Override
        public String getUsername() {

            return username;
        }

        public void setUsername(String username) {

            this.username = username;
        }

        @Override
        public String getDisplayName() {

            return displayName;
        }

        public void setDisplayName(String displayName) {

            this.displayName = displayName;
        }

        @Override
        public Boolean getEnabled() {

            return enabled;
        }

        public void setEnabled(Boolean enabled) {

            this.enabled = enabled;
        }
    }

}
