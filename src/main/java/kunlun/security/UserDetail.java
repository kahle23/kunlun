package kunlun.security;

import java.io.Serializable;

/**
 * The interface for the definition of user detail.
 * @author Kahle
 */
public interface UserDetail extends Serializable {

    /**
     * Get the user id.
     * @return The user id
     */
    Object getId();

    /**
     * Get the user type.
     * @return The user type
     */
    Object getType();

    /**
     * Get the user account.
     * @return The user account
     */
    String getAccount();

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
