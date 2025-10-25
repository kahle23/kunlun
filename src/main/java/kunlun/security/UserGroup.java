package kunlun.security;

import java.io.Serializable;

/**
 * The interface for the definition of user group.
 * @author Kahle
 */
public interface UserGroup extends Serializable {

    /**
     * Get the user group id.
     * @return The user group id
     */
    Object getId();

    /**
     * Get the user group type.
     * @return The user group type
     */
    Object getType();

    /**
     * Get the user group name.
     * @return The user group name
     */
    String getName();

    /**
     * Indicates whether the user group is enabled or disabled.
     * @return True if the user group is enabled, false otherwise
     */
    Boolean getEnabled();

}
