/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.security;

import java.io.Serializable;

/**
 * The token manager.
 * @author Kahle
 */
public interface TokenManager {

    /**
     * Build token string based on the incoming token data.
     * @param token The incoming token data
     * @return The built token string
     */
    String buildToken(Token token);

    /**
     * Parse token string into token data.
     * @param token The token string
     * @return The token data
     */
    Token  parseToken(String token);

    /**
     * Verify the token's status.
     * The status:
     *      1 normal
     *      0 abnormal
     *     -1 not token
     *     -2 invalid token
     *     -3 token expired
     *     -4 be replaced
     *     -5 kick out
     *     -6 token freeze
     * @param token The token string
     * @return The token's status
     */
    int    verifyToken(String token);

    /**
     * Delete the token.
     * @param token The token string
     * @param reason The reason for deletion
     */
    void   deleteToken(String token, Integer reason);

    /**
     * Refresh the token's expiration time.
     * @param token The token string
     * @return The refreshed token string or null
     */
    Object refreshToken(String token);


    /**
     * The interface for the definition of token data.
     * @author Kahle
     */
    interface Token extends Serializable {

        /**
         * Get the token value.
         * @return The token value
         */
        String getValue();

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

    }

}
