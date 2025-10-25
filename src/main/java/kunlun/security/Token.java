package kunlun.security;

import java.io.Serializable;

/**
 * The interface for the definition of token data.
 * @author Kahle
 */
public interface Token extends Serializable {

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
