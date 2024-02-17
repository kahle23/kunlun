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
     * Judge whether the "user" has multiple resources (and).
     * @param subjectId The unique identification of the "user"
     * @param subjectType The "user" type (can null) (for example, toC user, toB user, or admin user)
     * @param operation The operation name or the operation object (can null)
     * @param resources The resources identifiers (or (resource + operation) objects) to be judged
     * @return The result of judgment
     */
    boolean hasPermissionAnd(Object subjectId, Object subjectType, Object operation, Object... resources);

    /**
     * Judge whether the "user" has multiple resources (or).
     * @param subjectId The unique identification of the "user"
     * @param subjectType The "user" type (can null) (for example, toC user, toB user, or admin user)
     * @param operation The operation name or the operation object (can null)
     * @param resources The resources identifiers (or (resource + operation) objects) to be judged
     * @return The result of judgment
     */
    boolean hasPermissionOr(Object subjectId, Object subjectType, Object operation, Object... resources);

}
