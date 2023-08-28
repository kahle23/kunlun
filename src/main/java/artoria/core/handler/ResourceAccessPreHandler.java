package artoria.core.handler;

import artoria.core.Handler;

/**
 * The handler before the resource is accessed.
 * It is generally used for signature verification,
 *      data decryption, login authentication, and permission authentication, etc.
 * @author Kahle
 */
public interface ResourceAccessPreHandler extends Handler {

    /**
     * Logical processing before a resource is accessed.
     * @param type It is used to distinguish resource access in different scenarios
     * @param resource The identifier of the resource being accessed
     * @param token The token carried when accessing the resource or null
     * @param arguments The arguments that may be used during preprocessing
     * @return The result of preprocessing (boolean in most scenarios)
     */
    Object handle(Object type, Object resource, String token, Object... arguments);

}
