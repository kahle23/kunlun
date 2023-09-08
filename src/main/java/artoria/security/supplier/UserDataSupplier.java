package artoria.security.supplier;

import artoria.core.Supplier;

import java.lang.reflect.Type;

/**
 * The user data supplier.
 * @author Kahle
 */
public interface UserDataSupplier extends Supplier {

    /**
     * Get the user data based on user id.
     * @param userId The unique identification of the user
     * @param userType The user type (for example, toC user, toB user, or admin user)
     * @param expectedType The expected type of user data
     * @return The user data
     */
    Object getUser(Object userId, Object userType, Type expectedType);

}
