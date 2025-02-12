/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.core;

/**
 * Provide the highest level of abstraction for access controller.
 *
 * Access control, the decision to allow or prohibit "a user" from doing something.
 * Subject: The subject is the initiator of the request. It can be a user, a process, an application, or a device.
 * Object:  The object is the recipient of the request. It can be an API, a file, a database, etc.
 * Request: The request is an operation performed by a subject on an object, such as reading, writing, executing, etc.
 *
 * Why not provide a way to get resources based on user id?
 * Because the "resources" associated with the user may be expressions, such as regular expressions.
 *
 * @see <a href="https://en.wikipedia.org/wiki/Access_control#Computer_security">Computer security</a>
 * @see <a href="https://en.wikipedia.org/wiki/Access_control#Access_control_models">Access control models</a>
 * @author Kahle
 */
public interface AccessController {

    /**
     * Judge whether the user has multiple permissions (and).
     * @param userId The unique identification of the user
     * @param userType The user type (can null) (for example, toC user, toB user, or admin user)
     * @param permission The permission identifier to be judged (such as "order_add")
     * @return The result of judgment
     */
    boolean hasPermission(Object userId, Object userType, String permission);

}
