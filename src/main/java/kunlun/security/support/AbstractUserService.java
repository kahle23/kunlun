/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.security.support;

import kunlun.security.UserDetail;
import kunlun.security.UserGroup;
import kunlun.security.UserService;

import java.util.Collection;

/**
 * The abstract user service.
 * @author Kahle
 */
public abstract class AbstractUserService implements UserService {

    @Override
    public Collection<String> getPermissions(Object userId, Object userType) {

        return null;
    }

    @Override
    public Collection<String> getUserGroups(Object userId, Object userType, Object groupType) {

        return null;
    }

    @Override
    public Collection<UserDetail> getUserDetails(Collection<?> userIds, Object userType) {

        throw new UnsupportedOperationException();
    }

    @Override
    public Collection<UserGroup> getUserGroups(Collection<?> groupIds, Object groupType) {

        throw new UnsupportedOperationException();
    }

    /**
     * The simple implementation class for user detail.
     * @author Kahle
     */
    public static class UserImpl implements UserDetail {
        private Object id;
        private Object type;
        private String account;
        private String displayName;
        private Boolean enabled;

        @Override
        public Object getId() {

            return id;
        }

        public void setId(Object id) {

            this.id = id;
        }

        @Override
        public Object getType() {

            return type;
        }

        public void setType(Object type) {

            this.type = type;
        }

        @Override
        public String getAccount() {

            return account;
        }

        public void setAccount(String account) {

            this.account = account;
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
