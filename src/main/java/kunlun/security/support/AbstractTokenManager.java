/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.security.support;

import kunlun.security.TokenManager;

/**
 * The abstract token manager.
 * @author Kahle
 */
public abstract class AbstractTokenManager implements TokenManager {

    @Override
    public void deleteToken(String token, Integer reason) {

    }

    @Override
    public Object refreshToken(String token) {

        return token;
    }

    /**
     * The simple implementation class for Token.
     * @author Kahle
     */
    public static class TokenImpl implements Token {
        private Object userType;
        private Object userId;
        private String value;

        public TokenImpl(String value, Object userId, Object userType) {
            this.userType = userType;
            this.userId = userId;
            this.value = value;
        }

        public TokenImpl(Object userId, Object userType) {

            this(null, userId, userType);
        }

        public TokenImpl(Object userId) {

            this(null, userId, null);
        }

        public TokenImpl() {

        }

        @Override
        public String getValue() {

            return value;
        }

        public void setValue(String value) {

            this.value = value;
        }

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
    }

}
