/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.core;

/**
 * Provide the highest level of abstraction for data controller.
 *
 * Data Access Control (DAC) is a security mechanism in the field of computer security
 *      that restricts and controls user access to data, ensuring data security, integrity,
 *      and preventing unauthorized access and data leaks.
 *
 * @author Kahle
 */
public interface DataController extends Strategy {

    /**
     * Get the specific rule for data permission control.
     * @param permission The permission identifier (such as "order_query")
     * @param userId The unique identification of the user
     * @param userType The user type (can null) (for example, toC user, toB user, or admin user)
     * @return The specific rule for data permission control
     */
    <T extends Rule> T getRule(String permission, Object userId, Object userType);

    /**
     * The specific rule for data permission control.
     * @author Kahle
     */
    interface Rule {

    }

}
